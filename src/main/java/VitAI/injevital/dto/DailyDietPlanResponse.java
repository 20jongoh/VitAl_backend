package VitAI.injevital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyDietPlanResponse {
    private List<DietPlanResponse> breakfast;
    private List<DietPlanResponse> lunch;
    private List<DietPlanResponse> dinner;
    private List<DietPlanResponse> snacks;
    private NutritionInfoDto totalNutrition;
}