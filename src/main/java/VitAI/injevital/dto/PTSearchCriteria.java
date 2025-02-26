package VitAI.injevital.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PTSearchCriteria {
    private String location;        // 지역
    private String specialty;       // 전문분야
    private Integer minPrice;       // 최소 가격
    private Integer maxPrice;       // 최대 가격
    private String trainerName;     // 트레이너 이름
    private String keyword;         // 검색 키워드 (제목/내용)
    private Boolean activeOnly;     // 활성화된 게시글만 검색할지 여부

    // 검색 조건이 설정되어 있는지 확인하는 메서드
    public boolean hasSearchCriteria() {
        return location != null ||
                specialty != null ||
                minPrice != null ||
                maxPrice != null ||
                trainerName != null ||
                keyword != null;
    }

    // 가격 범위 유효성 검사
    public boolean isValidPriceRange() {
        if (minPrice == null || maxPrice == null) {
            return true;
        }
        return minPrice <= maxPrice;
    }
}