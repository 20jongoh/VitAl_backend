package VitAI.injevital.service;

import VitAI.injevital.dto.*;
import VitAI.injevital.entity.Member;
import VitAI.injevital.entity.PTBooking;
import VitAI.injevital.entity.PTPost;
import VitAI.injevital.enumSet.BookingStatus;
import VitAI.injevital.enumSet.PTStatus;
import VitAI.injevital.repository.MemberRepository;
import VitAI.injevital.repository.PTBookingRepository;
import VitAI.injevital.repository.PTPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PTService {
    private final PTPostRepository ptPostRepository;
    private final PTBookingRepository ptBookingRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    // PT 게시글 작성
    @Transactional
    public PTPost createPost(String memberId, PTPostRequest request) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        List<String> imageUrls = uploadImages(request.getImages());

        PTPost post = PTPost.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .location(request.getLocation())
                .price(request.getPrice())
                .images(imageUrls)
                .specialty(request.getSpecialty())
                .status(PTStatus.ACTIVE)
                .build();

        return ptPostRepository.save(post);
    }

    // PT 게시글 수정
    @Transactional
    public PTPost updatePost(String memberId, Long postId, PTPostRequest request) {
        PTPost post = ptPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 작성자 검증
        if (!post.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");
        }

        // 기존 이미지 삭제 및 새 이미지 업로드
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            deleteImages(post.getImages());
            List<String> newImageUrls = uploadImages(request.getImages());
            post.setImages(newImageUrls);
        }

        post.updatePost(request.getTitle(), request.getContent(),
                request.getLocation(), request.getPrice(),
                request.getSpecialty());

        return ptPostRepository.save(post);
    }

    // PT 게시글 삭제
    @Transactional
    public void deletePost(String memberId, Long postId) {
        PTPost post = ptPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 작성자 검증
        if (!post.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("게시글 삭제 권한이 없습니다.");
        }

        // 이미지 삭제
        deleteImages(post.getImages());

        // 게시글 삭제
        ptPostRepository.delete(post);
    }

    // PT 게시글 목록 조회 (검색/필터링)
    public Page<PTPostResponse> getPosts(PTSearchCriteria criteria, Pageable pageable) {
        return ptPostRepository.findBySearchCriteria(criteria, pageable)
                .map(this::convertToResponse);
    }

    // PT 게시글 상세 조회
    public PTPostResponse getPost(Long postId) {
        PTPost post = ptPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return convertToResponse(post);
    }

    // PT 예약 생성
    @Transactional
    public PTBooking createBooking(String memberId, PTBookingRequest request) {
        Member trainee = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        PTPost post = ptPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 예약 가능 여부 확인
        validateBooking(post, request.getBookingDate());

        PTBooking booking = PTBooking.builder()
                .post(post)
                .trainer(post.getMember())
                .trainee(trainee)
                .bookingDate(request.getBookingDate())
                .status(BookingStatus.PENDING)
                .amount(post.getPrice())
                .build();

        return ptBookingRepository.save(booking);
    }

    // 예약 상태 변경
    @Transactional
    public PTBooking updateBookingStatus(String memberId, Long bookingId, BookingStatus status) {
        PTBooking booking = ptBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // 권한 검증
        validateBookingStatusUpdate(memberId, booking, status);

        booking.setStatus(status);
        return ptBookingRepository.save(booking);
    }

    // 트레이너의 예약 목록 조회
    public List<PTBookingResponse> getTrainerBookings(String trainerId) {
        return ptBookingRepository.findByTrainerMemberId(trainerId).stream()
                .map(this::convertToBookingResponse)
                .collect(Collectors.toList());
    }

    // 수강생의 예약 목록 조회
    public List<PTBookingResponse> getTraineeBookings(String traineeId) {
        return ptBookingRepository.findByTraineeMemberId(traineeId).stream()
                .map(this::convertToBookingResponse)
                .collect(Collectors.toList());
    }

    // 이미지 업로드 헬퍼 메서드
    private List<String> uploadImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream()
                .map(s3Service::uploadFile)
                .collect(Collectors.toList());
    }

    // 이미지 삭제 헬퍼 메서드
    private void deleteImages(List<String> imageUrls) {
        if (imageUrls != null) {
            imageUrls.forEach(s3Service::deleteFile);
        }
    }

    // 예약 가능 여부 검증
    private void validateBooking(PTPost post, LocalDateTime bookingDate) {
        // 게시글 상태 확인
        if (post.getStatus() != PTStatus.ACTIVE) {
            throw new RuntimeException("현재 예약이 불가능한 게시글입니다.");
        }

        // 예약 시간 중복 확인
        boolean isTimeSlotAvailable = ptBookingRepository
                .findOverlappingBookings(post.getMember().getMemberId(), bookingDate)
                .isEmpty();

        if (!isTimeSlotAvailable) {
            throw new RuntimeException("해당 시간에는 이미 예약이 있습니다.");
        }
    }

    // 예약 상태 변경 권한 검증
    private void validateBookingStatusUpdate(String memberId, PTBooking booking, BookingStatus newStatus) {
        boolean isTrainer = booking.getTrainer().getMemberId().equals(memberId);
        boolean isTrainee = booking.getTrainee().getMemberId().equals(memberId);

        if (!isTrainer && !isTrainee) {
            throw new RuntimeException("예약 상태 변경 권한이 없습니다.");
        }

        // 트레이너만 CONFIRMED 상태로 변경 가능
        if (newStatus == BookingStatus.CONFIRMED && !isTrainer) {
            throw new RuntimeException("트레이너만 예약을 확정할 수 있습니다.");
        }

        // 현재 상태에 따른 변경 가능 여부 확인
        validateStatusTransition(booking.getStatus(), newStatus);
    }

    // 예약 상태 전이 규칙 검증
    private void validateStatusTransition(BookingStatus currentStatus, BookingStatus newStatus) {
        // 예약 상태 전이 규칙 정의
        if (currentStatus == BookingStatus.COMPLETED ||
                currentStatus == BookingStatus.CANCELLED) {
            throw new RuntimeException("완료되거나 취소된 예약은 상태를 변경할 수 없습니다.");
        }
    }

    // PT 게시글 응답 변환
    private PTPostResponse convertToResponse(PTPost post) {
        return PTPostResponse.builder()
                .id(post.getId())
                .trainerName(post.getMember().getMemberName())
                .profileImageUrl(post.getMember().getProfileImageUrl())
                .title(post.getTitle())
                .content(post.getContent())
                .location(post.getLocation())
                .price(post.getPrice())
                .imageUrls(post.getImages())
                .specialty(post.getSpecialty())
                .createdAt(post.getCreatedDate())
                .build();
    }

    // 예약 응답 변환
    private PTBookingResponse convertToBookingResponse(PTBooking booking) {
        return PTBookingResponse.builder()
                .id(booking.getId())
                .postId(booking.getPost().getId())
                .trainerName(booking.getTrainer().getMemberName())
                .traineeName(booking.getTrainee().getMemberName())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .amount(booking.getAmount())
                .build();
    }
}