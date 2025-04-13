package kr.concert.domain.seat;

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
        List<Seat> seats = seatRepository.getSeatsOfSchedule(scheduleId);

        if (seats.isEmpty()) throw new ReservationException.SeatNotExistException();
        return seats;
    }
}
