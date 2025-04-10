package kr.concert.domain.concert;

import kr.concert.domain.reservation.Reservation;

import java.util.List;

public interface ConcertRepository {

    List<Concert> getConcerts();
}
