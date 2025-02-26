package VitAI.injevital.service;

import VitAI.injevital.dto.*;
import VitAI.injevital.entity.Member;
import VitAI.injevital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleIntegrationService {
    private final MemberRepository memberRepository;
    private final ExerciseRecordService exerciseRecordService;
    private final DietPlanService dietPlanService;
    private final ScheduleService scheduleService;

    @Transactional(readOnly = true)
    public DailyScheduleDetailResponse getDailyScheduleDetails(String memberId, LocalDateTime date) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 운동 기록 조회
        List<ExerciseHistoryResponse> exercises = exerciseRecordService.getDailyExercises(memberId, date);

        // 식단 계획 조회
        DailyDietPlanResponse dietPlans = dietPlanService.getDailyDietPlan(memberId, date);

        // 기타 일정 조회
        List<ScheduleDTO> otherSchedules = scheduleService.getDailySchedules(memberId, date.toLocalDate())
                .stream()
                .map(ScheduleDTO::from)
                .toList();

        return DailyScheduleDetailResponse.builder()
                .exercises(exercises)
                .dietPlans(dietPlans)
                .otherSchedules(otherSchedules)
                .date(date)
                .build();
    }
}