package VitAI.injevital.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class PTPostRequest {
    private String title;
    private String content;
    private String location;
    private Integer price;
    private List<MultipartFile> images;
    private String specialty;
}