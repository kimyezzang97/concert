package kr.concert.infra.impl;


import kr.concert.domain.concert.Concert;
import kr.concert.domain.concert.ConcertRepository;
import kr.concert.domain.reservation.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertRespositoryImpl implements ConcertRepository {

    @Override
    public List<Concert> getConcerts() {
        return List.of();
    }
}
