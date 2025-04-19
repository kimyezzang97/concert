package kr.concert.domain.schedule.service;

import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.schedule.repo.ScheduleRepository;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    public List<Schedule> getSchedulesOfConcert(Long concertId) {
        List<Schedule> schedules = scheduleRepository.findAllByConcert_ConcertId(concertId);

        if (schedules.isEmpty()) throw new ReservationException.ScheduleNotExistException();
        return schedules;
    }
}
