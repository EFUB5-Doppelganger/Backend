package com.efub.doppelganger.reservation.dto.response;

import com.efub.doppelganger.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationResponseDto {
    private Long reservationId;
    private LocalDateTime createdAt;

    public static ReservationResponseDto of(Reservation reservation) {
        return ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}