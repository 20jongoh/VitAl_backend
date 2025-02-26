package VitAI.injevital.repository;

import VitAI.injevital.entity.ExerciseRecord;
import VitAI.injevital.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {
    List<ExerciseRecord> findByMemberOrderByExerciseDateDesc(Member member);

    List<ExerciseRecord> findByMemberAndExerciseDateBetweenOrderByExerciseDateDesc(
            Member member,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT er FROM ExerciseRecord er WHERE er.member = :member AND er.exercise.id = :exerciseId ORDER BY er.exerciseDate DESC")
    List<ExerciseRecord> findByMemberAndExerciseOrderByDateDesc(
            @Param("member") Member member,
            @Param("exerciseId") Long exerciseId
    );
}