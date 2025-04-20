package kr.concert.domain.reservation.repo;

import kr.concert.domain.reservation.ReservationStatus;
import kr.concert.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> getReservation(Long reservationId);

    Reservation save(Reservation reservation);

    List<Reservation> getExpireReservations(ReservationStatus status, LocalDateTime now);
}
