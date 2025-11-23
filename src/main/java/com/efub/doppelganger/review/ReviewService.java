package com.efub.doppelganger.review;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.repository.AccommodationRepository;
import com.efub.doppelganger.member.domain.Member;
import com.efub.doppelganger.reservation.Reservation;
import com.efub.doppelganger.reservation.ReservationRepository;
import com.efub.doppelganger.review.dto.AccommodationReviewsResponseDto;
import com.efub.doppelganger.review.dto.MyReviewResponseDto;
import com.efub.doppelganger.review.dto.ReviewCreateRequestDto;
import com.efub.doppelganger.review.dto.ReviewResponseDto;
import com.efub.doppelganger.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AccommodationRepository accommodationRepository;
    private final ReservationRepository reservationRepository;
    private final CurrentMemberUtil currentMemberUtil;

    public ReviewResponseDto createReview(Long accommodationId, Long reservationId,
                                          ReviewCreateRequestDto requestDto) {

        Member writer = currentMemberUtil.getCurrentMember();

        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new IllegalArgumentException("AccommodationID 존재하지 않습니다."));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("ReservationID 존재하지 않습니다."));

        Review review = Review.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .score(requestDto.getScore())
                .writer(writer)
                .accommodation(accommodation)
                .reservation(reservation)
                .build();

        Review saved = reviewRepository.save(review);
        return ReviewResponseDto.from(saved);
    }

    // 내가 쓴 리뷰들 조회
    public List<MyReviewResponseDto> getMyReviews() {

        Long memberId = currentMemberUtil.getCurrentMember().getId();

        List<Review> myReviews =
                reviewRepository.findByWriterIdOrderByCreatedAtDesc(memberId);

        return myReviews.stream()
                .map(MyReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    // 특정 숙소의 리뷰 목록 조회
    public AccommodationReviewsResponseDto getAccommodationReviews(Long accommodationId) {

        // 1. 숙소 존재 여부 확인 (404 Not Found 오류 방지)
        accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new NoSuchElementException("Accommodation not found with id: " + accommodationId));
        // A001 오류 처리를 위해 Controller에서 404로 매핑되어야 함

        // 2. 리뷰 데이터 조회 및 통계 계산
        List<Review> reviews = reviewRepository.findByAccommodationIdOrderByCreatedAtDesc(accommodationId);

        // 평균 평점 계산 (null 체크 포함)
        Double avgScore = reviewRepository.findAverageScoreByAccommodationId(accommodationId);
        double averageRating = (avgScore != null) ? Math.round(avgScore * 100) / 100.0 : 0.0;
        int totalReviews = reviews.size();

        // DTO 매핑
        List<AccommodationReviewsResponseDto.ReviewDetailDto> reviewDtos = reviews.stream()
                .map(AccommodationReviewsResponseDto.ReviewDetailDto::from)
                .collect(Collectors.toList());

        return AccommodationReviewsResponseDto.builder()
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .reviews(reviewDtos)
                .build();
    }
}

