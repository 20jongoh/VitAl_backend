package VitAI.injevital.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PTBookingRequest {
    private Long postId;
    private LocalDateTime bookingDate;
}