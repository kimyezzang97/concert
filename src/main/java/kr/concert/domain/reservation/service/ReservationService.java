package kr.concert.domain.reservation.service;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.reservation.ReservationStatus;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.reservation.repo.ReservationRepository;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Member member, Seat seat) {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);
        Reservation reservation = Reservation.create(member, seat, expiredAt);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(@PathVariable Long reservationId) {
        return reservationRepository.getReservation(reservationId)
                .orElseThrow(ReservationException.ReservationNotExistException::new);
    }

    public List<Seat> cancelReservation() {
        List<Reservation> expireList = reservationRepository.getExpireReservations(ReservationStatus.TEMP, LocalDateTime.now());

        List<Seat> seatIdsOfExpiredReservationList = new ArrayList<>();
        for(Reservation reservation : expireList) {
            seatIdsOfExpiredReservationList.add(reservation.getSeat());
            reservation.cancelReservation();
        }

        return seatIdsOfExpiredReservationList;
    }
}
