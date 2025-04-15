package kr.concert.domain.concert;

import java.util.List;

public interface ConcertRepository {

    List<Concert> findAll();
}
