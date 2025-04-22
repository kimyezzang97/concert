package kr.concert.domain.schedule.repo;

import kr.concert.domain.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    List<Schedule> findAllByConcert_ConcertId(Long concertId);

    Schedule save(Schedule schedule);
}
