package VitAI.injevital.dto;

import VitAI.injevital.entity.NutritionInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class NutritionInfoDto {
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;

    public NutritionInfoDto() {
        this.calories = 0.0;
        this.protein = 0.0;
        this.carbs = 0.0;
        this.fat = 0.0;
    }

    public NutritionInfoDto(Double calories, Double protein, Double carbs, Double fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    // NutritionInfo 엔티티로부터 DTO를 생성하는 정적 팩토리 메서드
    public static NutritionInfoDto from(NutritionInfo nutritionInfo) {
        return new NutritionInfoDto(
                nutritionInfo.getCalories(),
                nutritionInfo.getProtein(),
                nutritionInfo.getCarbs(),
                nutritionInfo.getFat()
        );
    }

    // DTO를 NutritionInfo 엔티티로 변환하는 메서드
    public NutritionInfo toEntity() {
        NutritionInfo nutritionInfo = new NutritionInfo();
        nutritionInfo.setCalories(this.calories);
        nutritionInfo.setProtein(this.protein);
        nutritionInfo.setCarbs(this.carbs);
        nutritionInfo.setFat(this.fat);
        return nutritionInfo;
    }
}