package com.efub.doppelganger.accommodation.controller;

import com.efub.doppelganger.accommodation.dto.request.AccommodationRegisterRequestDto;
import com.efub.doppelganger.accommodation.dto.response.AccommodationDetailResponseDto;
import com.efub.doppelganger.accommodation.dto.response.AccommodationListResponseDto;
import com.efub.doppelganger.accommodation.service.AccommodationService;
import com.efub.doppelganger.global.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    // 위치로 숙소 조회
    @GetMapping
    public ResponseEntity<AccommodationListResponseDto> searchAccommodation(@RequestParam String query,
                                                                            @RequestParam int page){
        AccommodationListResponseDto responseDto = accommodationService.getAccommodations(query, page);
        return ResponseEntity.ok(responseDto);
    }

    // 숙소 카드 정렬
    @GetMapping("/card")
    public ResponseEntity<AccommodationListResponseDto> sortAccommodation(@RequestParam String sortBy,
                                                                          @RequestParam int page) {
        AccommodationListResponseDto responseDto = accommodationService.getSortedAccommodations(sortBy, page);
        return ResponseEntity.ok(responseDto);
    }

    // 숙소 등록
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerAccommodation(
            @ModelAttribute AccommodationRegisterRequestDto requestDto
    ) {
        accommodationService.registerAccommodation(requestDto);
        return ResponseEntity.ok("숙소 등록이 완료되었습니다.");
    }

    // 숙소 상세 정보 조회
    @GetMapping("/{accommodationId}")
    public ResponseEntity<AccommodationDetailResponseDto> getAccommodationDetail(@PathVariable Long accommodationId) {
        AccommodationDetailResponseDto responseDto = accommodationService.findAccommodationDetail(accommodationId);
        return ResponseEntity.ok(responseDto);
    }

    // 존재하지 않는 숙소 조회 (404 Not Found) 핸들러
    @ExceptionHandler(java.util.NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(java.util.NoSuchElementException e) {

        // A001: 숙소 없음 오류 코드 사용
        ErrorResponse error = new ErrorResponse("A001", "Accommodation not found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
