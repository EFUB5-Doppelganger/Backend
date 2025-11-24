package com.efub.doppelganger.reservation.dto.response;

import com.efub.doppelganger.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReservationResponseDto {

    private Long reservationId;
    private String accommodationName;
    private Long accommodationId;
    private String imageUrl;

    private String location;
    private int price;

    private String checkIn;
    private String checkOut;

    private int guests;
    private int totalPayment;

    public static MyReservationResponseDto of(Reservation reservation) {

        var acc = reservation.getAccommodation();

        // 대표 이미지 1개
        String img = null;
        if (acc.getAccommodationImageList() != null && !acc.getAccommodationImageList().isEmpty()) {
            img = acc.getAccommodationImageList().get(0).getImgUrl();
        }

        return MyReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .accommodationId(acc.getId())
                .accommodationName(acc.getName())
                .location(acc.getLocation())
                .price(acc.getPrice())
                .imageUrl(img)
                .checkIn(reservation.getCheckIn().toString())
                .checkOut(reservation.getCheckOut().toString())
                .guests(reservation.getGuests())
                .totalPayment(reservation.getTotalPayment())
                .build();
    }
}
