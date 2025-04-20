package kr.concert.domain.concert.repo;

import kr.concert.domain.concert.entity.Concert;

import java.util.List;

public interface ConcertRepository {

    List<Concert> findAll();

    Concert save(Concert concert);
}
