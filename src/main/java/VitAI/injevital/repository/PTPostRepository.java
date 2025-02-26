package VitAI.injevital.repository;

import VitAI.injevital.entity.PTPost;
import VitAI.injevital.dto.PTSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PTPostRepository extends JpaRepository<PTPost, Long> {

    // 트레이너의 모든 게시글 조회
    List<PTPost> findByMemberMemberId(String memberId);

    // 지역별 게시글 조회
    List<PTPost> findByLocation(String location);

    // 전문분야별 게시글 조회
    List<PTPost> findBySpecialty(String specialty);

    // 가격 범위로 게시글 조회
    List<PTPost> findByPriceBetween(Integer minPrice, Integer maxPrice);

    // 검색 조건에 따른 게시글 조회
    @Query("SELECT p FROM PTPost p " +
            "WHERE (:#{#criteria.location} IS NULL OR p.location = :#{#criteria.location}) " +
            "AND (:#{#criteria.specialty} IS NULL OR p.specialty = :#{#criteria.specialty}) " +
            "AND (:#{#criteria.minPrice} IS NULL OR p.price >= :#{#criteria.minPrice}) " +
            "AND (:#{#criteria.maxPrice} IS NULL OR p.price <= :#{#criteria.maxPrice}) " +
            "AND (:#{#criteria.trainerName} IS NULL OR p.member.memberName LIKE %:#{#criteria.trainerName}%) " +
            "AND (:#{#criteria.keyword} IS NULL OR p.title LIKE %:#{#criteria.keyword}% " +
            "OR p.content LIKE %:#{#criteria.keyword}%)")
    Page<PTPost> findBySearchCriteria(@Param("criteria") PTSearchCriteria criteria, Pageable pageable);

    // 제목으로 게시글 검색
    List<PTPost> findByTitleContaining(String keyword);

    // 제목 또는 내용으로 게시글 검색
    @Query("SELECT p FROM PTPost p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<PTPost> findByTitleOrContentContaining(@Param("keyword") String keyword);
}