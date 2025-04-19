package kr.concert.domain.concert.service;

import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Transactional(readOnly = true)
    public List<Concert> getConcerts() {
        List<Concert> concerts = concertRepository.findAll();

        if (concerts.isEmpty()) throw new ReservationException.ConcertNotExistException();
        return concerts;
    }

}
