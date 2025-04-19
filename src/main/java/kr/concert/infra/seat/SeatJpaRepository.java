package kr.concert.infra.seat;

import kr.concert.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findAllBySchedule_ScheduleId(Long scheduleId);

    Optional<Seat> findBySeatId(Long seatId);
}
