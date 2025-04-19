package kr.concert.domain.seat.repo;

import kr.concert.domain.seat.entity.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {

    List<Seat> findAllBySchedule_ScheduleId(Long scheduleId);

    Optional<Seat> findBySeatId(Long seatId);
}
