package kr.concert.infra.impl;

import kr.concert.domain.seat.Seat;
import kr.concert.domain.seat.SeatRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatRepositoryImpl implements SeatRepository {
    @Override
    public List<Seat> getSeatsOfSchedule(Long scheduleId) {
        return List.of();
    }
}
