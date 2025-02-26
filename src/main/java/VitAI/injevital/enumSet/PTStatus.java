package VitAI.injevital.enumSet;

public enum PTStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String description;

    PTStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}