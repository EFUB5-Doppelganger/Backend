package com.efub.doppelganger.accommodation.controller;

import com.efub.doppelganger.accommodation.dto.AccommodationListResponseDto;
import com.efub.doppelganger.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
}
