package kr.concert.domain.seat;

import java.util.List;

public interface SeatRepository {

    List<Seat> findAllBySchedule_ScheduleId(Long scheduleId);
}
