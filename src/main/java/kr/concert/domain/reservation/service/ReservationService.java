package kr.concert.domain.reservation.service;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.reservation.repo.ReservationRepository;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Member member, Seat seat) {
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(1);
        Reservation reservation = Reservation.create(member, seat, expiredAt);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(@PathVariable Long reservationId) {
        return reservationRepository.getReservation(reservationId)
                .orElseThrow(ReservationException.ReservationNotExistException::new);
    }
}
