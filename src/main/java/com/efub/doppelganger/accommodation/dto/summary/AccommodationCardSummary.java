package com.efub.doppelganger.accommodation.dto.summary;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.domain.AccommodationImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccommodationCardSummary {
    private Long id;
    private String name;
    private double rating;
    private String image;
    private String location;
    private int price;

    public static AccommodationCardSummary from (Accommodation accommodation) {
        AccommodationImage accommodationImage = accommodation.getAccommodationImageList().get(0); // 대표 이미지 1개만 사용

        return AccommodationCardSummary.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .rating(accommodation.getRating())
                .image(accommodationImage.getImgUrl())
                .location(accommodation.getLocation())
                .price(accommodation.getPrice())
                .build();
    }
}
