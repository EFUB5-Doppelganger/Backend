package com.efub.doppelganger.accommodation.dto.summary;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.domain.AccommodationImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccommodationSummary {
    private Long id;
    private String name;
    private double rating;
    private String image;
    private String location;
    private int price;
    private int reviewCnt;
    private int maxGuests;
    private int bedroom;
    private int bed;
    private int bathroom;

    public static AccommodationSummary from (Accommodation accommodation) {
        AccommodationImage accommodationImage = accommodation.getAccommodationImageList().get(0); // 대표 이미지 1개만 사용

        int reviewCnt = 0;
        if (accommodation.getReviewList() != null) {
            reviewCnt = accommodation.getReviewList().size();
        }

        return AccommodationSummary.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .rating(accommodation.getRating())
                .image(accommodationImage.getImgUrl())
                .location(accommodation.getLocation())
                .price(accommodation.getPrice())
                .reviewCnt(reviewCnt)
                .maxGuests(accommodation.getMaxGuests())
                .bedroom(accommodation.getBedroom())
                .bed(accommodation.getBed())
                .bathroom(accommodation.getBathroom())
                .build();
    }
}
