package com.efub.doppelganger.accommodation.service;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.domain.AccommodationImage;
import com.efub.doppelganger.accommodation.dto.request.AccommodationRegisterRequestDto;
import com.efub.doppelganger.accommodation.dto.response.AccommodationDetailResponseDto;
import com.efub.doppelganger.accommodation.dto.response.AccommodationListResponseDto;
import com.efub.doppelganger.accommodation.dto.summary.AccommodationSummary;
import com.efub.doppelganger.accommodation.repository.AccommodationRepository;
import com.efub.doppelganger.global.s3.S3Service;
import com.efub.doppelganger.member.domain.Member;
import com.efub.doppelganger.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccommodationService {

    private final S3Service s3Service;
    private final CurrentMemberUtil currentMemberUtil;
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

    // 숙소 등록
    @Transactional
    public void registerAccommodation(AccommodationRegisterRequestDto requestDto) {
        Member host = currentMemberUtil.getCurrentMember();

        Accommodation accommodation = Accommodation.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .host(host)
                .location(requestDto.getLocation())
                .address(requestDto.getAddress())
                .price(requestDto.getPrice())
                .maxGuests(requestDto.getMaxGuests())
                .bedroom(requestDto.getBedroom())
                .bed(requestDto.getBed())
                .bathroom(requestDto.getBathroom())
                .build();
        accommodationRepository.save(accommodation);

        if (requestDto.getImages() != null && !requestDto.getImages().isEmpty()) {
            int idx = 0;
            for (MultipartFile file : requestDto.getImages()) {
                String url = s3Service.uploadFile(file, "accommodations/" + accommodation.getId());
                AccommodationImage img = AccommodationImage.builder()
                        .accommodation(accommodation)
                        .imgUrl(url)
                        .displayOrder(idx++)
                        .build();
                accommodation.getAccommodationImageList().add(img);
            }
        }
    }

    // 숙소 상세 정보 조회
    public AccommodationDetailResponseDto findAccommodationDetail(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new NoSuchElementException("Accommodation not found with id: " + accommodationId));

        if (accommodation.getAccommodationImageList().isEmpty()) {
        }

        return AccommodationDetailResponseDto.of(accommodation);
    }

    // 내가 등록한 숙소 조회
    public List<AccommodationSummary> getMyAccommodations() {

        Long memberId = currentMemberUtil.getCurrentMember().getId();

        List<Accommodation> accommodationList =
                accommodationRepository.findByHostId(memberId);

        return accommodationList.stream()
                .map(AccommodationSummary::from)
                .toList();
    }
}
