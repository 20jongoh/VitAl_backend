package VitAI.injevital.dto;

import VitAI.injevital.entity.DietPlan.MealType;
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
public class DietPlanResponse {
    private Long id;
    private LocalDateTime planDate;
    private MealType mealType;
    private List<String> menuItems;
    private String description;
    private NutritionInfoDto nutritionInfo;
}