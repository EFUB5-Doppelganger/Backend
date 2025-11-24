package com.efub.doppelganger.accommodation.repository;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    // 위치로 숙소 검색
    Page<Accommodation> findByLocationContaining(String location, Pageable pageable);

    // memberId로 조회
    List<Accommodation> findByHostId(Long memberId);
}
