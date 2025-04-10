package kr.concert.interfaces.reservation;

import kr.concert.application.reservation.ReservationFacade;
import kr.concert.domain.reservation.ReservationService;
import kr.concert.interfaces.presentation.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

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
}
