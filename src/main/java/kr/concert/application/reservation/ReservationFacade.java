package kr.concert.application.reservation;

import kr.concert.domain.concert.ConcertService;
import kr.concert.domain.schedule.ScheduleService;
import kr.concert.interfaces.reservation.ReservationResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationFacade {

    private ConcertService concertService;
    private ScheduleService scheduleService;

    public ReservationFacade(ConcertService concertService, ScheduleService scheduleService) {
        this.concertService = concertService;
        this.scheduleService = scheduleService;
    }

    // 콘서트 목록 조회
    public List<ReservationResponse.GetConcerts> getConcerts() {

        return concertService.getConcerts().stream()
                .map(c -> new ReservationResponse.GetConcerts(c.getConcertId(), c.getConcertName(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 날짜 of 콘서트 목록 조회
    public List<ReservationResponse.GetScheduleOfConcert> getSchedulesOfConcert(Long concertId) {

        return scheduleService.getSchedulesOfConcert(concertId).stream()
                .map(c -> new ReservationResponse.GetScheduleOfConcert(c.getScheduleId(), c.getScheduleDate(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
