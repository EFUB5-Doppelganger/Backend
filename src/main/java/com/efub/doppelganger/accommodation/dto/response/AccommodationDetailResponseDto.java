package com.efub.doppelganger.accommodation.dto.response;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class AccommodationDetailResponseDto {
    private Long id;
    private String name;
    private String description;
    private int dailyPrice; // DB: price
    private double rating;
    private int maxGuests;
    private String address;
    private List<String> amenities; // 편의시설 목록 (bedroom, bed, bathroom 포함)
    private List<AccommodationPhotoDto> photos;

    // 내부 클래스로 사진 정보 정의
    @Getter
    @Builder
    public static class AccommodationPhotoDto {
        private String url;
        private int displayOrder;
    }

    public static AccommodationDetailResponseDto of(Accommodation accommodation) {
        // 편의시설 (Amenities) 목록 생성
        // DB 필드를 기반으로 문자열 리스트로 변환하여 JSON에 포함
        List<String> amenitiesList = List.of(
                "침실 수: " + accommodation.getBedroom(),
                "침대 수: " + accommodation.getBed(),
                "욕실 수: " + accommodation.getBathroom()
                // 기타 편의시설 필드가 있다면 여기에 추가
        );

        // 사진 목록 변환 (display_order 순으로 정렬 후 매핑)
        List<AccommodationPhotoDto> photoDtos = accommodation.getAccommodationImageList().stream()
                .sorted((img1, img2) -> Integer.compare(img1.getDisplayOrder(), img2.getDisplayOrder()))
                .map(image -> AccommodationPhotoDto.builder()
                        .url(image.getImgUrl()) // DB: imgUrl -> JSON: url
                        .displayOrder(image.getDisplayOrder())
                        .build())
                .collect(Collectors.toList());

        return AccommodationDetailResponseDto.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .dailyPrice(accommodation.getPrice()) // DB: price -> JSON: dailyPrice
                .rating(accommodation.getRating())
                .maxGuests(accommodation.getMaxGuests())
                .address(accommodation.getAddress())
                .amenities(amenitiesList)
                .photos(photoDtos)
                .build();
    }
}
