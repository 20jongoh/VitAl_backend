package VitAI.injevital.enumSet;

public enum BookingStatus {
    PENDING("대기중"),         // 예약 신청 상태
    CONFIRMED("확정"),        // 트레이너가 예약을 확정한 상태
    COMPLETED("완료"),        // PT 수업이 완료된 상태
    CANCELLED("취소"),        // 예약이 취소된 상태
    NO_SHOW("노쇼"),          // 예약자가 나타나지 않은 상태
    TRAINER_CANCELLED("트레이너 취소");  // 트레이너가 취소한 상태

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}