package VitAI.injevital.enumSet;

public enum ExerciseIntensity {
    LIGHT("가벼움"),
    MODERATE("적당함"),
    HARD("힘듦"),
    VERY_HARD("매우 힘듦");

    private final String description;

    ExerciseIntensity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}