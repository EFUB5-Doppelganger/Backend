package com.efub.doppelganger.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 특정 숙소의 주어진 기간과 겹치는 예약 목록을 조회
    @Query("SELECT r FROM Reservation r WHERE r.accommodation.id = :accommodationId " +
            "AND r.checkIn < :checkOut AND r.checkOut > :checkIn")
    List<Reservation> findOverlappingReservations(
            @Param("accommodationId") Long accommodationId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}