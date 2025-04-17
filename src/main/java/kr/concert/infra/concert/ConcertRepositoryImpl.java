package kr.concert.infra.concert;

import kr.concert.domain.concert.Concert;
import kr.concert.domain.concert.ConcertRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    public ConcertRepositoryImpl(ConcertJpaRepository concertJpaRepository) {
        this.concertJpaRepository = concertJpaRepository;
    }

    @Override
    public List<Concert> findAll() {
        return concertJpaRepository.findAll();
    }
}
