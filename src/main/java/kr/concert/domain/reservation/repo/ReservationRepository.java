package kr.concert.domain.reservation.repo;

import kr.concert.domain.reservation.entity.Reservation;

import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> getReservation(Long reservationId);

    Reservation save(Reservation reservation);
}
