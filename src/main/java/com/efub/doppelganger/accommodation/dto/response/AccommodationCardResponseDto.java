package com.efub.doppelganger.accommodation.dto.response;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.dto.summary.AccommodationCardSummary;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class AccommodationCardResponseDto {

    private List<AccommodationCardSummary> accommodations;
    private int currentPage;
    private int totalPage;
    private int totalCount;

    public static AccommodationCardResponseDto from(Page<Accommodation> accommodations,
                                                    int currentPage,
                                                    int totalPage,
                                                    int totalCount) {
        return AccommodationCardResponseDto.builder()
                .accommodations(accommodations.stream().map(AccommodationCardSummary::from).toList())
                .currentPage(currentPage)
                .totalPage(totalPage)
                .totalCount(totalCount)
                .build();
    }
}
