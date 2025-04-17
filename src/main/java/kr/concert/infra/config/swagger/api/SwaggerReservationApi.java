package kr.concert.infra.config.swagger.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.concert.interfaces.presentation.ApiResponse;
import kr.concert.interfaces.reservation.ReservationRequest;
import kr.concert.interfaces.reservation.ReservationResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Reservation", description = "콘서트 예약 관련 API")
public interface SwaggerReservationApi {

    @Operation(summary = "콘서트 조회", description = "콘서트 목록을 조회합니다.")
    ApiResponse<List<ReservationResponse.GetConcerts>> getConcerts();

    @Operation(summary = "스케줄 조회", description = "콘서트의 스케줄을 조회합니다.")
    ApiResponse<List<ReservationResponse.GetScheduleOfConcert>> getSchedulesOfConcert(@PathVariable Long concertId);

    @Operation(summary = "좌석 조회", description = "콘서트의 좌석을 조회합니다.")
    ApiResponse<List<ReservationResponse.GetSeatsOfSchedule>> getSeatsOfSchedule(@PathVariable Long scheduleId);

    @Operation(summary = "좌석 예약", description = "콘서트의 좌석을 예약합니다.")
    ApiResponse<ReservationResponse.Reserve> reserve(@RequestBody ReservationRequest.Reserve reservation);
}
