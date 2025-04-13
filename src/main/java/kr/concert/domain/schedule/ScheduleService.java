package kr.concert.domain.schedule;

import kr.concert.domain.concert.Concert;
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
        List<Schedule> schedules = scheduleRepository.getSchedulesOfConcert(concertId);

        if (schedules.isEmpty()) throw new ReservationException.ScheduleNotExistException();
        return schedules;
    }
}
