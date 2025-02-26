package VitAI.injevital.controller;

import VitAI.injevital.dto.DailyScheduleDetailResponse;
import VitAI.injevital.service.ScheduleIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Schedule Integration", description = "통합 일정 관리 API")
@RestController
@RequestMapping("/api/schedules/integrated")
@RequiredArgsConstructor
public class ScheduleIntegrationController {

    private final ScheduleIntegrationService scheduleIntegrationService;

    @Operation(summary = "일일 통합 일정 조회", description = "특정 날짜의 운동, 식단, 기타 일정을 모두 조회합니다.")
    @GetMapping("/daily")
    public ResponseEntity<DailyScheduleDetailResponse> getDailyScheduleDetails(
            @RequestParam String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(scheduleIntegrationService.getDailyScheduleDetails(memberId, date));
    }
}