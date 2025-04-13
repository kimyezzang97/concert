package kr.concert.infra.impl;

import kr.concert.domain.concert.Concert;
import kr.concert.domain.schedule.Schedule;
import kr.concert.domain.schedule.ScheduleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    @Override
    public List<Schedule> getSchedulesOfConcert(Long concertId) {
        return List.of();
    }
}
