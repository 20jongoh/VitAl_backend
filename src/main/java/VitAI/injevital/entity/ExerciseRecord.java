package VitAI.injevital.entity;

import VitAI.injevital.enumSet.ExerciseIntensity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercise_records")
public class ExerciseRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(nullable = false)
    private LocalDateTime exerciseDate;

    private Integer sets;
    private Integer reps;
    private Double weight;  // 무게(kg)

    @Column(columnDefinition = "TEXT")
    private String note;  // 운동 메모

    private Integer restTime;  // 휴식 시간(초)

    @Enumerated(EnumType.STRING)
    private ExerciseIntensity intensity;  // 운동 강도

    private Double duration;  // 운동 시간(분)

    @Column(columnDefinition = "TEXT")
    private String feedback;  // 피드백/느낀점
}
