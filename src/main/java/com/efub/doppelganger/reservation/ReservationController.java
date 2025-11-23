package com.efub.doppelganger.reservation;

import com.efub.doppelganger.reservation.dto.request.ReservationCheckRequestDto;
import com.efub.doppelganger.reservation.dto.request.ReservationRequestDto;
import com.efub.doppelganger.reservation.dto.response.ReservationCheckResponseDto;
import com.efub.doppelganger.reservation.dto.response.ReservationResponseDto;
import com.efub.doppelganger.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
import com.efub.doppelganger.global.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 가능 날짜 선택 및 가격 확인
    @PostMapping("/accommodations/{accommodationId}/check")
    public ResponseEntity<ReservationCheckResponseDto> checkReservation(
            @PathVariable Long accommodationId,
            @RequestBody ReservationCheckRequestDto requestDto
    ) {
        ReservationCheckResponseDto responseDto = reservationService.checkAvailabilityAndPrice(accommodationId, requestDto);
        return ResponseEntity.ok(responseDto); // 200 OK (가능 여부와 상관없이)
    }

    // 예약 요청 API
    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto responseDto = reservationService.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto); // 201 Created
    }

    // 모듈 내부에서 IllegalStateException 발생 시 409 Conflict 처리
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleReservationConflict(IllegalStateException e) {
        // R201: 예약 충돌 오류 코드
        ErrorResponse error = new ErrorResponse("R201", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}