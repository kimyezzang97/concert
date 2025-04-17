package kr.concert.domain.schedule;

import kr.concert.domain.concert.Concert;

import java.util.List;

public interface ScheduleRepository {

    List<Schedule> findAllByConcert_ConcertId(Long concertId);
}
