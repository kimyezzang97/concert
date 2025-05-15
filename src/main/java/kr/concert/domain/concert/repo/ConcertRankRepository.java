package kr.concert.domain.concert.repo;

import kr.concert.interfaces.concert.ConcertResponse;

import java.util.List;
import java.util.Set;

public interface ConcertRankRepository {

    public void createConcertOpenDate(Long concertId);

    List<ConcertResponse.Rank> getConcertRanks();

    public void removeRank(String value);

    Set<String> getRawRankingValues();

    public void decreaseSeatCount(Long concertId, String concertName);
}
