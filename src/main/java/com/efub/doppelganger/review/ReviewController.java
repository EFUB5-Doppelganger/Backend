package com.efub.doppelganger.review;

import com.efub.doppelganger.global.ErrorResponse;
import com.efub.doppelganger.review.dto.AccommodationReviewsResponseDto;
import com.efub.doppelganger.review.dto.MyReviewResponseDto;
import com.efub.doppelganger.review.dto.ReviewCreateRequestDto;
import com.efub.doppelganger.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/accommodations/{accommodationId}/reservations/{reservationId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long accommodationId,
            @PathVariable Long reservationId,
            @RequestBody ReviewCreateRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.createReview(accommodationId, reservationId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("reviews/me")
    public ResponseEntity<List<MyReviewResponseDto>> getMyReviews() {
        List<MyReviewResponseDto> response = reviewService.getMyReviews();
        return ResponseEntity.ok(response);
    }

    // 숙소 리뷰 목록 조회
    @GetMapping("/accommodations/{accommodationId}/reviews")
    public ResponseEntity<AccommodationReviewsResponseDto> getAccommodationReviews(
            @PathVariable Long accommodationId
    ) {
        AccommodationReviewsResponseDto responseDto = reviewService.getAccommodationReviews(accommodationId);
        return ResponseEntity.ok(responseDto);
    }
}
