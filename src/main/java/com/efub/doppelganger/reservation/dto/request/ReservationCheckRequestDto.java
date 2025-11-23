package com.efub.doppelganger.reservation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationCheckRequestDto {
    @NotNull(message = "체크인 날짜는 필수입니다.")
    @Future(message = "체크인 날짜는 현재 이후여야 합니다.")
    private LocalDate checkIn;

    @NotNull(message = "체크아웃 날짜는 필수입니다.")
    private LocalDate checkOut;

    @Min(value = 1, message = "게스트는 최소 1명 이상이어야 합니다.")
    private int guests;
}