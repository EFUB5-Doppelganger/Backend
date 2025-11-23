package com.efub.doppelganger.review.dto;

import com.efub.doppelganger.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class AccommodationReviewsResponseDto {
    private final double averageRating;
    private final int totalReviews;
    private final List<ReviewDetailDto> reviews;

    // 내부 리뷰 상세 DTO
    @Getter
    @Builder
    public static class ReviewDetailDto {
        private final Long reviewId;
        private final String reviewerName;
        private final int score;
        private final String content;
        private final String createdAt;

        public static ReviewDetailDto from(Review review) {
            return ReviewDetailDto.builder()
                    .reviewId(review.getId())
                    .reviewerName(review.getWriter().getNickname()) // Member 엔티티 조인 필요
                    .score(review.getScore())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();
        }
    }
}