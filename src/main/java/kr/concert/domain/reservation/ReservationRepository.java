package kr.concert.domain.reservation;

import kr.concert.domain.member.Member;
import kr.concert.domain.queue.Queue;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> getReservation(Long reservationId);

    Reservation save(Reservation reservation);
}
