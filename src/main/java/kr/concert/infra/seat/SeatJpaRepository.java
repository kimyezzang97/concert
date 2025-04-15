package kr.concert.infra.seat;

import kr.concert.domain.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findAllBySchedule_ScheduleId(Long scheduleId);
}
