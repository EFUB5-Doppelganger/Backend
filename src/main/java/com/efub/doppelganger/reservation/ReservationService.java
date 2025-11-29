package com.efub.doppelganger.reservation;

import com.efub.doppelganger.accommodation.domain.Accommodation;
import com.efub.doppelganger.accommodation.repository.AccommodationRepository;
import com.efub.doppelganger.member.domain.Member;
import java.time.LocalDate;
import com.efub.doppelganger.reservation.Reservation;
import com.efub.doppelganger.reservation.ReservationRepository;
import com.efub.doppelganger.reservation.dto.request.ReservationCheckRequestDto;
import com.efub.doppelganger.reservation.dto.request.ReservationRequestDto;
import com.efub.doppelganger.reservation.dto.response.MyReservationResponseDto;
import com.efub.doppelganger.reservation.dto.response.ReservationCheckResponseDto;
import com.efub.doppelganger.reservation.dto.response.ReservationResponseDto;
import com.efub.doppelganger.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AccommodationRepository accommodationRepository;
    private final CurrentMemberUtil currentMemberUtil;

    // DB에 숙소가 존재하는지 확인 (없으면 404)
    private Accommodation findAccommodationById(Long accommodationId) {
        return accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new NoSuchElementException("Accommodation not found with id: " + accommodationId));
    }

    // 예약 가능 여부 및 가격을 확인
    public ReservationCheckResponseDto checkAvailabilityAndPrice(Long accommodationId, ReservationCheckRequestDto requestDto) {
        Accommodation accommodation = findAccommodationById(accommodationId);

        // 1. 날짜 유효성 검사 (체크아웃이 체크인보다 늦어야 함)
        if (!requestDto.getCheckOut().isAfter(requestDto.getCheckIn())) {
            throw new IllegalArgumentException("체크아웃 날짜는 체크인 날짜 이후여야 합니다."); // R101 오류 유발 가능
        }

        // 2. 인원 수 확인
        if (requestDto.getGuests() > accommodation.getMaxGuests()) {
            return ReservationCheckResponseDto.builder()
                    .isAvailable(false)
                    .reason("최대 인원(" + accommodation.getMaxGuests() + "명)을 초과했습니다.")
                    .totalPrice(0)
                    .build();
        }

        // 3. 재고(inventory) 확인
        boolean isInventoryAvailable = checkInventory(accommodationId, requestDto.getCheckIn(), requestDto.getCheckOut());

        if (!isInventoryAvailable) {
            return ReservationCheckResponseDto.builder()
                    .isAvailable(false)
                    .reason("선택하신 기간은 이미 예약이 완료되었습니다.")
                    .totalPrice(0)
                    .build();
        }

        // 4. 가격 계산 (임시: 1박 가격 * 숙박 일수)
        long nights = ChronoUnit.DAYS.between(requestDto.getCheckIn(), requestDto.getCheckOut());
        int calculatedPrice = accommodation.getPrice() * (int) nights; // price 필드 사용 (DB: price)
        int finalPrice = calculatedPrice + (int) (calculatedPrice * 0.1); // 수수료/세금 10% 가정

        return ReservationCheckResponseDto.builder()
                .isAvailable(true)
                .totalPrice(finalPrice)
                .detail(nights + "박 기준, 세금 포함 금액")
                .build();
    }

    // 실제 재고 확인 로직 구현..
    private boolean checkInventory(Long accommodationId, LocalDate checkIn, LocalDate checkOut) {
        // 겹치는 예약이 있는지 DB 조회
        List<Reservation> overlappingReservations =
                reservationRepository.findOverlappingReservations(accommodationId, checkIn, checkOut);

        // 겹치는 예약 목록이 비어있다면 (size가 0이라면) 재고가 있는 것으로 간주
        return overlappingReservations.isEmpty();
    }


    // 최종 예약 요청 및 저장
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        // 1. 현재 로그인된 회원 정보 가져오기
        Member member = currentMemberUtil.getCurrentMember();

        // 2. 숙소 존재 확인
        Accommodation accommodation = findAccommodationById(requestDto.getAccommodationId());

        reservationRepository.flush();
        // 3. 예약 가능 여부 재확인 및 충돌 검사 로직 추가
        boolean isAvailable = checkInventory(
                requestDto.getAccommodationId(),
                requestDto.getCheckIn(),
                requestDto.getCheckOut()
        );

        if (!isAvailable) {
            // 예약이 불가능하면 IllegalStateException
            // 이 예외는 Controller의 @ExceptionHandler에서 409 Conflict로 처리됩니다.
            throw new IllegalStateException("Accommodation inventory is insufficient for the requested dates");
        }

        // 4. 예약 객체 생성 (충돌이 없으므로 진행)
        Reservation reservation = Reservation.builder()
                .accommodation(accommodation)
                .member(member)
                .checkIn(requestDto.getCheckIn())
                .checkOut(requestDto.getCheckOut())
                .guests(requestDto.getGuests())
                .totalPayment(requestDto.getTotalPayment())
                .visited(false)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponseDto.of(savedReservation);
    }

    @Transactional(readOnly = true)
    public List<MyReservationResponseDto> getMyReservations() {

        Member member = currentMemberUtil.getCurrentMember();

        List<Reservation> reservations = reservationRepository.findByMember(member);

        return reservations.stream()
                .map(MyReservationResponseDto::of)
                .toList();
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        // 현재 로그인한 사용자
        Member member = currentMemberUtil.getCurrentMember();

        // 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 본인 예약인지 체크
        if (!reservation.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("본인의 예약만 취소할 수 있습니다.");
        }

        // 리뷰가 존재하는지 체크
        int reviewCount = reservationRepository.countReviewsByReservationId(reservationId);
        if (reviewCount > 0) {
            throw new IllegalStateException("리뷰가 존재하는 예약은 삭제할 수 없습니다.");
        }

        // 리뷰가 없다면 정상적으로 삭제
        reservationRepository.delete(reservation);
    }


}