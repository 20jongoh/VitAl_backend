package VitAI.injevital.repository;

import VitAI.injevital.entity.DietPlan;
import VitAI.injevital.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    List<DietPlan> findByMemberAndPlanDateBetweenOrderByPlanDateAsc(
            Member member,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<DietPlan> findByMemberAndPlanDateAndMealType(
            Member member,
            LocalDateTime planDate,
            DietPlan.MealType mealType
    );
}