package com.efub.doppelganger.review.dto;


import com.efub.doppelganger.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyReviewResponseDto {

    private Long reviewId;
    private String accommodationName;
    private int score;
    private String content;
    private LocalDateTime createdAt;

    public static MyReviewResponseDto from(Review review) {
        return MyReviewResponseDto.builder()
                .reviewId(review.getId())
                .accommodationName(review.getAccommodation().getName())
                .score(review.getScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
