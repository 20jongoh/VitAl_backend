package VitAI.injevital.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file) {
        validateFile(file);
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = createObjectMetadata(file);

        try {
            uploadToS3(file, fileName, objectMetadata);
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("빈 파일입니다.");
        }
        validateFileExtension(file.getOriginalFilename());
    }

    private void validateFileExtension(String fileName) {
        String extension = getExtension(fileName);
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

        // 디버깅을 위한 로그 추가
        System.out.println("파일명: " + fileName);
        System.out.println("확장자: " + extension);
        System.out.println("허용된 확장자 목록: " + allowedExtensions);

        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new RuntimeException("지원하지 않는 파일 형식입니다.");
        }
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString() + getExtension(originalFileName);
    }

    private String getExtension(String fileName) {
        try {
            // 파일명에서 마지막 점(.) 이후의 부분을 추출할 때 점을 포함하지 않도록 수정
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        } catch (StringIndexOutOfBoundsException e) {
            throw new RuntimeException("잘못된 형식의 파일입니다.");
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private void uploadToS3(MultipartFile file, String fileName, ObjectMetadata metadata) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}