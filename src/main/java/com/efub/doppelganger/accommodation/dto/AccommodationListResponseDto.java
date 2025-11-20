package com.efub.doppelganger.accommodation.dto;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.dto.summary.AccommodationSummary;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class AccommodationListResponseDto {

    private List<AccommodationSummary> accommodations;
    private int currentPage;
    private int totalPage;
    private int totalCount;

    public static AccommodationListResponseDto from(Page<Accommodation> accommodations,
                                                    int currentPage,
                                                    int totalPage,
                                                    int totalCount) {
        return AccommodationListResponseDto.builder()
                .accommodations(accommodations.stream().map(AccommodationSummary::from).toList())
                .currentPage(currentPage)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .build();
    }
}
