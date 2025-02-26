package VitAI.injevital.service;

import VitAI.injevital.dto.ExerciseRecordRequest;
import VitAI.injevital.dto.ExerciseRecordResponse;
import VitAI.injevital.dto.ExerciseHistoryResponse;
import VitAI.injevital.entity.Exercise;
import VitAI.injevital.entity.ExerciseRecord;
import VitAI.injevital.entity.Member;
import VitAI.injevital.repository.ExerciseRecordRepository;
import VitAI.injevital.repository.ExerciseRepository;
import VitAI.injevital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseRecordService {
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public ExerciseRecordResponse recordExercise(ExerciseRecordRequest request) {
        Member member = memberRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("운동을 찾을 수 없습니다."));

        ExerciseRecord record = ExerciseRecord.builder()
                .member(member)
                .exercise(exercise)
                .exerciseDate(request.getExerciseDate())
                .sets(request.getSets())
                .reps(request.getReps())
                .weight(request.getWeight())
                .note(request.getNote())
                .restTime(request.getRestTime())
                .intensity(request.getIntensity())
                .duration(request.getDuration())
                .feedback(request.getFeedback())
                .build();

        ExerciseRecord savedRecord = exerciseRecordRepository.save(record);
        return convertToResponse(savedRecord);
    }

    @Transactional(readOnly = true)
    public List<ExerciseHistoryResponse> getExerciseHistory(String memberId, Long exerciseId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        List<ExerciseRecord> records = exerciseRecordRepository
                .findByMemberAndExerciseOrderByDateDesc(member, exerciseId);

        return records.stream()
                .map(this::convertToHistoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExerciseHistoryResponse> getDailyExercises(String memberId, LocalDateTime date) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        List<ExerciseRecord> records = exerciseRecordRepository
                .findByMemberAndExerciseDateBetweenOrderByExerciseDateDesc(member, startOfDay, endOfDay);

        return records.stream()
                .map(this::convertToHistoryResponse)
                .collect(Collectors.toList());
    }

    private ExerciseRecordResponse convertToResponse(ExerciseRecord record) {
        return ExerciseRecordResponse.builder()
                .id(record.getId())
                .exerciseName(record.getExercise().getName())
                .exerciseDate(record.getExerciseDate())
                .sets(record.getSets())
                .reps(record.getReps())
                .weight(record.getWeight())
                .note(record.getNote())
                .restTime(record.getRestTime())
                .intensity(record.getIntensity())
                .duration(record.getDuration())
                .feedback(record.getFeedback())
                .exerciseType(record.getExercise().getPart())
                .targetMuscle(record.getExercise().getDescription())
                .build();
    }

    private ExerciseHistoryResponse convertToHistoryResponse(ExerciseRecord record) {
        String summary = String.format("%d세트 X %d회", record.getSets(), record.getReps());
        if (record.getWeight() != null && record.getWeight() > 0) {
            summary += String.format(" @ %.1fkg", record.getWeight());
        }

        return ExerciseHistoryResponse.builder()
                .recordId(record.getId())
                .exerciseName(record.getExercise().getName())
                .exerciseDate(record.getExerciseDate())
                .summary(summary)
                .feedback(record.getFeedback())
                .intensity(record.getIntensity())
                .build();
    }
}