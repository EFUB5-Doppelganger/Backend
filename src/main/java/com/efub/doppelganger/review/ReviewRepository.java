package com.efub.doppelganger.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByWriterIdOrderByCreatedAtDesc(Long memberId);

    // 특정 숙소의 모든 리뷰를 최신순으로 조회
    List<Review> findByAccommodationIdOrderByCreatedAtDesc(Long accommodationId);

    //  특정 숙소의 평균 평점 계산
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.accommodation.id = :accommodationId")
    Double findAverageScoreByAccommodationId(@Param("accommodationId") Long accommodationId);
}
