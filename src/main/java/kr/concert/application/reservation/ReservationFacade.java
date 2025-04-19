package kr.concert.application.reservation;

import kr.concert.domain.concert.service.ConcertService;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.service.MemberService;
import kr.concert.domain.queue.service.QueueService;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.reservation.service.ReservationService;
import kr.concert.domain.schedule.service.ScheduleService;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.service.SeatService;
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
    private final QueueService queueService;
    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationFacade(ConcertService concertService, ScheduleService scheduleService, SeatService seatService, QueueService queueService,
                             ReservationService reservationService, MemberService memberService) {
        this.concertService = concertService;
        this.scheduleService = scheduleService;
        this.seatService = seatService;
        this.queueService = queueService;
        this.reservationService = reservationService;
        this.memberService = memberService;
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
                .map(c -> new ReservationResponse.GetScheduleOfConcert(
                        c.getScheduleId(),
                        c.getConcert().getConcertName(),
                        c.getScheduleAt(),
                        c.getCreatedAt())
                )
                .collect(Collectors.toList());
    }

    // 좌석 of 날짜 목록 조회
    @Transactional(readOnly = true)
    public List<ReservationResponse.GetSeatsOfSchedule> getSeatsOfSchedule(Long scheduleId) {

        return seatService.getSeatsOfSchedule(scheduleId).stream()
                .map(c -> new ReservationResponse.GetSeatsOfSchedule(
                        c.getSchedule().getConcert().getConcertName(),
                        c.getSeatId(),
                        c.getSchedule().getScheduleId(),
                        c.getSeatNumber(),
                        c.getSeatPrice(),
                        c.isSeatStatus(),
                        c.getCreatedAt())
                )
                .collect(Collectors.toList());
    }

    // 좌석 예약
    @Transactional(rollbackFor = Exception.class)
    public ReservationResponse.Reserve reserve(Long seatId, Long memberId, String token){
        queueService.checkToken(token, memberId);
        seatService.reserveSeat(seatId);

        Member member = memberService.getMember(memberId);
        Seat seat = seatService.getSeat(seatId);

        Reservation reservation = reservationService.createReservation(member, seat);

        return new ReservationResponse.Reserve(reservation.getReservationId(), reservation.getReservationStatus(), reservation.getExpiredAt());
    }
}
