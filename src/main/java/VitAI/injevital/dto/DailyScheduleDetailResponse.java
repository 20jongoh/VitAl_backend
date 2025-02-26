package VitAI.injevital.dto;

import VitAI.injevital.entity.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyScheduleDetailResponse {
    private List<ExerciseHistoryResponse> exercises;
    private DailyDietPlanResponse dietPlans;
    private List<ScheduleDTO> otherSchedules;
    private LocalDateTime date;
}