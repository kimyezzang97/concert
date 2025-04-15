package kr.concert.infra.schedule;

import kr.concert.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByConcert_ConcertId(Long concertId);
}
