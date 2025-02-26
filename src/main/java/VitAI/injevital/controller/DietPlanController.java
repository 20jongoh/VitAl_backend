package VitAI.injevital.controller;

import VitAI.injevital.dto.*;
import VitAI.injevital.service.DietPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Diet Plans", description = "식단 계획 관리 API")
@RestController
@RequestMapping("/api/diet-plans")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietPlanService dietPlanService;

    @Operation(summary = "식단 계획 저장", description = "새로운 식단 계획을 저장합니다.")
    @PostMapping
    public ResponseEntity<DietPlanResponse> createDietPlan(
            @RequestBody DietPlanRequest request) {
        return ResponseEntity.ok(dietPlanService.createDietPlan(request));
    }

    @Operation(summary = "식단 계획 수정", description = "기존 식단 계획을 수정합니다.")
    @PutMapping("/{planId}")
    public ResponseEntity<DietPlanResponse> updateDietPlan(
            @PathVariable Long planId,
            @RequestBody DietPlanUpdateRequest request) {
        request.setPlanId(planId);
        return ResponseEntity.ok(dietPlanService.updateDietPlan(request));
    }

    @Operation(summary = "일일 식단 계획 조회", description = "특정 날짜의 모든 식단 계획을 조회합니다.")
    @GetMapping("/daily")
    public ResponseEntity<DailyDietPlanResponse> getDailyDietPlan(
            @RequestParam String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(dietPlanService.getDailyDietPlan(memberId, date));
    }
}