package VitAI.injevital.dto;

import VitAI.injevital.enumSet.ExerciseIntensity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecordRequest {
    private String memberId;
    private Long exerciseId;
    private LocalDateTime exerciseDate;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private String note;
    private Integer restTime;
    private ExerciseIntensity intensity;
    private Double duration;
    private String feedback;
}