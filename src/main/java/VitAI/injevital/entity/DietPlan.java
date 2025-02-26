package VitAI.injevital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diet_plans")
public class DietPlan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDateTime planDate;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @ElementCollection
    @CollectionTable(name = "diet_plan_menus", joinColumns = @JoinColumn(name = "diet_plan_id"))
    private List<String> menuItems;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Embedded
    private NutritionInfo nutritionInfo;

    public enum MealType {
        BREAKFAST("아침"),
        LUNCH("점심"),
        DINNER("저녁"),
        SNACK("간식");

        private final String description;

        MealType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}

