package VitAI.injevital.controller;

import VitAI.injevital.dto.ExerciseRecordRequest;
import VitAI.injevital.dto.ExerciseRecordResponse;
import VitAI.injevital.dto.ExerciseHistoryResponse;
import VitAI.injevital.service.ExerciseRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Exercise Records", description = "운동 기록 관리 API")
@RestController
@RequestMapping("/api/exercise-records")
@RequiredArgsConstructor
public class ExerciseRecordController {

    private final ExerciseRecordService exerciseRecordService;

    @Operation(summary = "운동 기록 저장", description = "새로운 운동 기록을 저장합니다.")
    @PostMapping
    public ResponseEntity<ExerciseRecordResponse> recordExercise(
            @RequestBody ExerciseRecordRequest request) {
        ExerciseRecordResponse response = exerciseRecordService.recordExercise(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 운동의 기록 조회", description = "특정 운동에 대한 모든 기록을 조회합니다.")
    @GetMapping("/history/{exerciseId}")
    public ResponseEntity<List<ExerciseHistoryResponse>> getExerciseHistory(
            @PathVariable Long exerciseId,
            @RequestParam String memberId) {
        List<ExerciseHistoryResponse> history =
                exerciseRecordService.getExerciseHistory(memberId, exerciseId);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "일일 운동 기록 조회", description = "특정 날짜의 모든 운동 기록을 조회합니다.")
    @GetMapping("/daily")
    public ResponseEntity<List<ExerciseHistoryResponse>> getDailyExercises(
            @RequestParam String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<ExerciseHistoryResponse> exercises =
                exerciseRecordService.getDailyExercises(memberId, date);
        return ResponseEntity.ok(exercises);
    }
}