package VitAI.injevital.dto;

import VitAI.injevital.enumSet.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PTBookingResponse {
    private Long id;                   // 예약 ID
    private Long postId;               // 게시글 ID
    private String trainerName;        // 트레이너 이름
    private String traineeName;        // 수강생 이름
    private LocalDateTime bookingDate; // 예약 일시
    private BookingStatus status;      // 예약 상태
    private Integer amount;            // 결제 금액
    private LocalDateTime createdAt;   // 예약 생성 시간
    private String trainerProfileUrl;  // 트레이너 프로필 이미지
    private String postTitle;          // PT 게시글 제목
}