package com.efub.doppelganger.reservation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationCheckResponseDto {
    private boolean isAvailable;
    private int totalPrice;
    private String detail;
    private String reason; // isAvailable이 false일 경우 사용
}