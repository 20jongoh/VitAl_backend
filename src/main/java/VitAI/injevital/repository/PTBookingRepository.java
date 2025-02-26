package VitAI.injevital.repository;

import VitAI.injevital.entity.PTBooking;
import VitAI.injevital.enumSet.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PTBookingRepository extends JpaRepository<PTBooking, Long> {

    // 트레이너의 예약 목록 조회
    List<PTBooking> findByTrainerMemberId(String trainerId);

    // 수강생의 예약 목록 조회
    List<PTBooking> findByTraineeMemberId(String traineeId);

    // 특정 상태의 예약 목록 조회
    List<PTBooking> findByStatus(BookingStatus status);

    // 특정 게시글의 예약 목록 조회
    List<PTBooking> findByPostId(Long postId);

    // 특정 날짜의 예약 목록 조회
    List<PTBooking> findByBookingDateBetween(LocalDateTime start, LocalDateTime end);

    // 특정 시간에 중복되는 예약이 있는지 확인
    @Query("SELECT b FROM PTBooking b " +
            "WHERE b.trainer.memberId = :trainerId " +
            "AND b.status NOT IN ('CANCELLED', 'COMPLETED') " +
            "AND b.bookingDate BETWEEN :bookingTime AND :bookingTime")
    List<PTBooking> findOverlappingBookings(
            @Param("trainerId") String trainerId,
            @Param("bookingTime") LocalDateTime bookingTime);

    // 트레이너의 특정 기간 예약 목록 조회
    @Query("SELECT b FROM PTBooking b " +
            "WHERE b.trainer.memberId = :trainerId " +
            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "ORDER BY b.bookingDate ASC")
    List<PTBooking> findTrainerBookingsBetweenDates(
            @Param("trainerId") String trainerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 수강생의 특정 기간 예약 목록 조회
    @Query("SELECT b FROM PTBooking b " +
            "WHERE b.trainee.memberId = :traineeId " +
            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "ORDER BY b.bookingDate ASC")
    List<PTBooking> findTraineeBookingsBetweenDates(
            @Param("traineeId") String traineeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}