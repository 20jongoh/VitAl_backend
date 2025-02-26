package VitAI.injevital.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionInfo {
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
}