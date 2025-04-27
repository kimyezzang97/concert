package kr.concert.application.reservation;

import kr.concert.domain.reservation.service.ReservationService;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.service.SeatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationScheduler {

    private final ReservationService reservationService;

    public ReservationScheduler(ReservationService reservationService, SeatService seatService) {
        this.reservationService = reservationService;
    }

    @Scheduled(fixedRate = 2000) // 2초마다 실행
    public void runExpireTempReservation() {
        // 만료기간이 지난 TEMP > EXPIRE 로 바꿔주기
        List<Seat> expiredSeatList = reservationService.cancelReservation();

        for(Seat seat : expiredSeatList) {
            seat.possibleSeatStatus();
        }
    }


}
