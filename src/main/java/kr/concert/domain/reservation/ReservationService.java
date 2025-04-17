package kr.concert.domain.reservation;

import kr.concert.domain.concert.Concert;
import kr.concert.domain.member.Member;
import kr.concert.domain.seat.Seat;
import kr.concert.interfaces.presentation.ApiResponse;
import kr.concert.interfaces.reservation.ReservationException;
import kr.concert.interfaces.reservation.ReservationRequest;
import kr.concert.interfaces.reservation.ReservationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

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
