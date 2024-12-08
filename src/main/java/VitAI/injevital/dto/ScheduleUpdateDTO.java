package VitAI.injevital.dto;

import VitAI.injevital.entity.ScheduleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUpdateDTO {
    @NotNull
    private Long scheduleId;

    @NotNull(message = "회원 ID는 필수입니다")
    private String memberId;

    @NotNull(message = "일정 날짜는 필수입니다")
    private LocalDateTime scheduleDate;

    @NotNull(message = "일정 제목은 필수입니다")
    private String title;

    private String content;

    @NotNull(message = "일정 타입은 필수입니다")
    private ScheduleType type;
}