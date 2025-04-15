package kr.concert.application.reservation;

import kr.concert.domain.concert.ConcertService;
import kr.concert.domain.schedule.ScheduleService;
import kr.concert.domain.seat.SeatService;
import kr.concert.interfaces.reservation.ReservationResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationFacade {

    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;

    public ReservationFacade(ConcertService concertService, ScheduleService scheduleService, SeatService seatService) {
        this.concertService = concertService;
        this.scheduleService = scheduleService;
        this.seatService = seatService;
    }

    // 콘서트 목록 조회
    public List<ReservationResponse.GetConcerts> getConcerts() {

        return concertService.getConcerts().stream()
                .map(c -> new ReservationResponse.GetConcerts(c.getConcertId(), c.getConcertName(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 스케줄 of 콘서트 목록 조회
    @Transactional(readOnly = true)
    public List<ReservationResponse.GetScheduleOfConcert> getSchedulesOfConcert(Long concertId) {

        return scheduleService.getSchedulesOfConcert(concertId).stream()
                .map(c -> new ReservationResponse.GetScheduleOfConcert(c.getScheduleId(), c.getConcert().getConcertName(), c.getScheduleAt(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 좌석 of 날짜 목록 조회
    public List<ReservationResponse.GetSeatsOfSchedule> getSeatsOfSchedule(Long scheduleId) {

        return seatService.getSeatsOfSchedule(scheduleId).stream()
                .map(c -> new ReservationResponse.GetSeatsOfSchedule(c.getSeatId(), c.getScheduleId(), c.getSeatNumber(), c.getSeatPrice(), c.isSeatStatus(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    // 좌석 예약
    public void reserve(Long seatId, Long memberId){

    }
}
