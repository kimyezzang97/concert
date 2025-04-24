package kr.concert.domain.seat.service;

import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.repo.SeatRepository;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsOfSchedule(Long scheduleId) {
        List<Seat> seats = seatRepository.findAllBySchedule_ScheduleId(scheduleId);

        if (seats.isEmpty()) throw new ReservationException.SeatNotExistException();
        return seats;
    }

    public void reserveSeat(Long seatId){
        Seat seat = seatRepository.findBySeatId(seatId)
                .orElseThrow(ReservationException.SeatNotExistException::new);

        if(!seat.isSeatStatus()) throw new ReservationException.SeatImpossibleException();

        seat.reserveSeatStatus();
    }

    public Seat getSeat(Long seatId){
        return seatRepository.findBySeatId(seatId)
                .orElseThrow(ReservationException.SeatNotExistException::new);
    }
}
