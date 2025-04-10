package kr.concert.application.reservation;

import kr.concert.domain.concert.ConcertService;
import kr.concert.interfaces.reservation.ReservationResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationFacade {

    private ConcertService concertService;

    public ReservationFacade(ConcertService concertService) {
        this.concertService = concertService;
    }

    public List<ReservationResponse.GetConcerts> getConcerts() {

        return concertService.getConcerts().stream()
                .map(c -> new ReservationResponse.GetConcerts(c.getConcertId(), c.getConcertName(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
