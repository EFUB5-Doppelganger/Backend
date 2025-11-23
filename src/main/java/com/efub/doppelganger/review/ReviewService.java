package com.efub.doppelganger.review;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.repository.AccommodationRepository;
import com.efub.doppelganger.member.domain.Member;
import com.efub.doppelganger.reservation.Reservation;
import com.efub.doppelganger.reservation.ReservationRepository;
import com.efub.doppelganger.review.dto.MyReviewResponseDto;
import com.efub.doppelganger.review.dto.ReviewCreateRequestDto;
import com.efub.doppelganger.review.dto.ReviewResponseDto;
import com.efub.doppelganger.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
}

