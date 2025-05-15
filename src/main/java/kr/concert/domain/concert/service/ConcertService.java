package kr.concert.domain.concert.service;

import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRankRepository;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.interfaces.concert.ConcertResponse;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertRankRepository concertRankRepository;

    public ConcertService(ConcertRepository concertRepository, ConcertRankRepository concertRankRepository) {
        this.concertRepository = concertRepository;
        this.concertRankRepository = concertRankRepository;
    }

    @Transactional(readOnly = true)
    public List<Concert> getConcerts() {
        List<Concert> concerts = concertRepository.findAll();

        if (concerts.isEmpty()) throw new ReservationException.ConcertNotExistException();
        return concerts;
    }

    @Transactional(rollbackFor = Exception.class)
    public ConcertResponse.CreateConcert createConcert(String concertName) {
        Concert concert = new Concert(null, concertName);
        concertRepository.save(concert);

        // concert open date > redis 추가
        concertRankRepository.createConcertOpenDate(concert.getConcertId());

        return new ConcertResponse.CreateConcert(concert.getConcertId(), concertName);
    }

    // 빠른 매진 랭킹 순위 (최근 7일)
    public List<ConcertResponse.Rank> getConcertRanks() {

        return concertRankRepository.getConcertRanks();
    }
}
