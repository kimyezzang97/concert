package kr.concert.interfaces.reservation;

import kr.concert.application.reservation.ReservationFacade;
import kr.concert.infra.config.swagger.api.SwaggerReservationApi;
import kr.concert.interfaces.presentation.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController implements SwaggerReservationApi {

    private final ReservationFacade reservationFacade;

    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    /**
     * Concert 목록 조회
     */
    @GetMapping("/concerts")
    public ApiResponse<List<ReservationResponse.GetConcerts>> getConcerts() {
        return new ApiResponse<>(true, 200, "콘서트 목록 조회 성공", reservationFacade.getConcerts());
    }

    /**
     * 스케줄 of Concert 목록 조회
     */
    @GetMapping("/concerts/{concertId}/schedules")
    public ApiResponse<List<ReservationResponse.GetScheduleOfConcert>> getSchedulesOfConcert(@PathVariable Long concertId) {
        return new ApiResponse<>(true, 200, "콘서트 스케줄 조회 성공", reservationFacade.getSchedulesOfConcert(concertId));
    }

    /**
     * 좌석 of 스케줄 of Concert 목록 조회
     */
    @GetMapping("/schedules/{scheduleId}/seats")
    public ApiResponse<List<ReservationResponse.GetSeatsOfSchedule>> getSeatsOfSchedule(@PathVariable Long scheduleId) {
        return new ApiResponse<>(true, 200, "콘서트 좌석 조회 성공", reservationFacade.getSeatsOfSchedule(scheduleId));
    }

    /**
     * 좌석 예약
     */
    @PostMapping("/seats")
    public ApiResponse<ReservationResponse.Reserve> reserve(@RequestBody ReservationRequest.Reserve reservation) {
        return new ApiResponse<>(true, 200, "콘서트 예약 성공",
                reservationFacade.reserve(reservation.seatId(), reservation.memberId(), reservation.token()));
    }
}
