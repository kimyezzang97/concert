package kr.concert.application.concert;

import kr.concert.domain.concert.repo.ConcertRankRepository;
import kr.concert.interfaces.concert.ConcertResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class ConcertScheduler {

    private final ConcertRankRepository concertRankRepository;

    public ConcertScheduler(ConcertRankRepository concertRankRepository) {
        this.concertRankRepository = concertRankRepository;
    }

    @Scheduled(cron = "0 0 * * * *") // 매시간 정각
    public void cleanupOldConcertRankings() {
        Set<String> values = concertRankRepository.getRawRankingValues();

        if (values == null || values.isEmpty()) return;

        LocalDate today = LocalDate.now();

        for (String value : values) {
            String[] parts = value.split("_");

            try {
                LocalDate date = LocalDate.parse(parts[2], DateTimeFormatter.BASIC_ISO_DATE);
                if (date.isBefore(today)) {
                    concertRankRepository.removeRank(value);
                }
            } catch (Exception e) {
                log.warn("cleanupOldConcertRankings 실패. value: {}", value, e);
            }
        }
    }
}
