package kr.concert.infra.schedule;

import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.schedule.repo.ScheduleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    public ScheduleRepositoryImpl(ScheduleJpaRepository scheduleJpaRepository) {
        this.scheduleJpaRepository = scheduleJpaRepository;
    }

    @Override
    public List<Schedule> findAllByConcert_ConcertId(Long concertId) {

        return scheduleJpaRepository.findAllByConcert_ConcertId(concertId);
    }
}
