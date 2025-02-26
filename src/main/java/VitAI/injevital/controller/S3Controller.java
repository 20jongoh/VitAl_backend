package VitAI.injevital.controller;

import VitAI.injevital.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "S3 File Management", description = "S3 파일 업로드 및 관리 API")
@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @Operation(
            summary = "파일 업로드",
            description = "이미지 파일을 AWS S3에 업로드합니다. 지원 형식: JPG, JPEG, PNG, GIF"
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @Parameter(
                    description = "업로드할 이미지 파일",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file
    ) {
        String fileUrl = s3Service.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    @Operation(
            summary = "파일 삭제",
            description = "S3에서 파일을 삭제합니다."
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(
            @Parameter(description = "삭제할 파일의 URL")
            @RequestParam("fileUrl") String fileUrl
    ) {
        s3Service.deleteFile(fileUrl);
        return ResponseEntity.ok().build();
    }
}