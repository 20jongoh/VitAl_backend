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
public class ExerciseHistoryResponse {
    private Long recordId;
    private String exerciseName;
    private LocalDateTime exerciseDate;
    private String summary;  // "3세트 X 12회 @ 60kg" 형식
    private String feedback;
    private ExerciseIntensity intensity;
}