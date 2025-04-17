package kr.concert.domain.seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {

    List<Seat> findAllBySchedule_ScheduleId(Long scheduleId);

    Optional<Seat> findBySeatId(Long seatId);
}
