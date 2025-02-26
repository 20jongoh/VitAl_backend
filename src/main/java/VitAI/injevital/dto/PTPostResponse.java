package VitAI.injevital.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PTPostResponse {
    private Long id;
    private String trainerName;
    private String profileImageUrl;
    private String title;
    private String content;
    private String location;
    private Integer price;
    private List<String> imageUrls;
    private String specialty;
    private LocalDateTime createdAt;
}