package VitAI.injevital.service;

import VitAI.injevital.dto.*;
import VitAI.injevital.entity.DietPlan;
import VitAI.injevital.entity.Member;
import VitAI.injevital.entity.NutritionInfo;
import VitAI.injevital.repository.DietPlanRepository;
import VitAI.injevital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietPlanService {
    private final DietPlanRepository dietPlanRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public DietPlanResponse createDietPlan(DietPlanRequest request) {
        Member member = memberRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        DietPlan dietPlan = DietPlan.builder()
                .member(member)
                .planDate(request.getPlanDate())
                .mealType(request.getMealType())
                .menuItems(request.getMenuItems())
                .description(request.getDescription())
                .nutritionInfo(convertToNutritionInfo(request.getNutritionInfo()))
                .build();

        DietPlan savedPlan = dietPlanRepository.save(dietPlan);
        return convertToResponse(savedPlan);
    }

    @Transactional
    public DietPlanResponse updateDietPlan(DietPlanUpdateRequest request) {
        DietPlan dietPlan = dietPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("식단 계획을 찾을 수 없습니다."));

        dietPlan.setMealType(request.getMealType());
        dietPlan.setMenuItems(request.getMenuItems());
        dietPlan.setDescription(request.getDescription());
        dietPlan.setNutritionInfo(convertToNutritionInfo(request.getNutritionInfo()));

        DietPlan updatedPlan = dietPlanRepository.save(dietPlan);
        return convertToResponse(updatedPlan);
    }

    @Transactional(readOnly = true)
    public DailyDietPlanResponse getDailyDietPlan(String memberId, LocalDateTime date) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        List<DietPlan> dailyPlans = dietPlanRepository
                .findByMemberAndPlanDateBetweenOrderByPlanDateAsc(member, startOfDay, endOfDay);

        return createDailyDietPlanResponse(dailyPlans);
    }

    private DailyDietPlanResponse createDailyDietPlanResponse(List<DietPlan> plans) {
        List<DietPlanResponse> breakfast = filterByMealType(plans, DietPlan.MealType.BREAKFAST);
        List<DietPlanResponse> lunch = filterByMealType(plans, DietPlan.MealType.LUNCH);
        List<DietPlanResponse> dinner = filterByMealType(plans, DietPlan.MealType.DINNER);
        List<DietPlanResponse> snacks = filterByMealType(plans, DietPlan.MealType.SNACK);

        NutritionInfoDto totalNutrition = calculateTotalNutrition(plans);

        return DailyDietPlanResponse.builder()
                .breakfast(breakfast)
                .lunch(lunch)
                .dinner(dinner)
                .snacks(snacks)
                .totalNutrition(totalNutrition)
                .build();
    }

    private List<DietPlanResponse> filterByMealType(List<DietPlan> plans, DietPlan.MealType mealType) {
        return plans.stream()
                .filter(plan -> plan.getMealType() == mealType)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private NutritionInfoDto calculateTotalNutrition(List<DietPlan> plans) {
        NutritionInfoDto total = NutritionInfoDto.builder()
                .calories(0.0)
                .protein(0.0)
                .carbs(0.0)
                .fat(0.0)
                .build();

        plans.stream()
                .map(DietPlan::getNutritionInfo)
                .filter(info -> info != null)
                .forEach(info -> {
                    total.setCalories(total.getCalories() + (info.getCalories() != null ? info.getCalories() : 0.0));
                    total.setProtein(total.getProtein() + (info.getProtein() != null ? info.getProtein() : 0.0));
                    total.setCarbs(total.getCarbs() + (info.getCarbs() != null ? info.getCarbs() : 0.0));
                    total.setFat(total.getFat() + (info.getFat() != null ? info.getFat() : 0.0));
                });

        return total;
    }

    private DietPlanResponse convertToResponse(DietPlan plan) {
        return DietPlanResponse.builder()
                .id(plan.getId())
                .planDate(plan.getPlanDate())
                .mealType(plan.getMealType())
                .menuItems(plan.getMenuItems())
                .description(plan.getDescription())
                .nutritionInfo(convertToNutritionInfoDto(plan.getNutritionInfo()))
                .build();
    }

    private NutritionInfo convertToNutritionInfo(NutritionInfoDto dto) {
        if (dto == null) {
            return new NutritionInfo();
        }
        return new NutritionInfo(
                dto.getCalories(),
                dto.getProtein(),
                dto.getCarbs(),
                dto.getFat()
        );
    }

    private NutritionInfoDto convertToNutritionInfoDto(NutritionInfo info) {
        if (info == null) {
            return new NutritionInfoDto();
        }
        return new NutritionInfoDto(
                info.getCalories(),
                info.getProtein(),
                info.getCarbs(),
                info.getFat()
        );
    }
}