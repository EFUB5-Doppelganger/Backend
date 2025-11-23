package com.efub.doppelganger.review;

import com.efub.doppelganger.review.dto.ReviewCreateRequestDto;
import com.efub.doppelganger.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{accommodationId}/reservations/{reservationId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long accommodationId,
            @PathVariable Long reservationId,
            @RequestBody ReviewCreateRequestDto requestDto
    ) {
        ReviewResponseDto responseDto = reviewService.createReview(accommodationId, reservationId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
