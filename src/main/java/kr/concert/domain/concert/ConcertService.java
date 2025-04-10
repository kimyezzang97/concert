package kr.concert.domain.concert;

import kr.concert.domain.member.MemberRepository;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> getConcerts() {
        List<Concert> concerts = concertRepository.getConcerts();

        if (concerts.isEmpty()) throw new ReservationException.ConcertNotExistException();
        return concerts;
    }

}
