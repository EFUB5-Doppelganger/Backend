package com.efub.doppelganger.accommodation.service;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.dto.AccommodationListResponseDto;
import com.efub.doppelganger.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    // 위치로 숙소 검색
    public AccommodationListResponseDto getAccommodations(String query, int page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<Accommodation> accommodationList = accommodationRepository.findByLocationContaining(query, pageable);

        int totalPages = accommodationList.getTotalPages();
        int totalCounts = (int) accommodationList.getTotalElements();

        return AccommodationListResponseDto.from(accommodationList, page, totalPages, totalCounts);
    }

    // 숙소 카드
    public AccommodationListResponseDto getSortedAccommodations(String sortBy, int page) {

        Sort sort = switch (sortBy) {
            case "createdAt" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            default -> Sort.unsorted();
        };
        Pageable pageable = PageRequest.of(page, 8, sort);
        Page<Accommodation> accommodationList = accommodationRepository.findAll(pageable);

        int totalPages = accommodationList.getTotalPages();
        int totalCounts = (int) accommodationList.getTotalElements();

        return AccommodationListResponseDto.from(accommodationList, page, totalPages, totalCounts);
    }
}
