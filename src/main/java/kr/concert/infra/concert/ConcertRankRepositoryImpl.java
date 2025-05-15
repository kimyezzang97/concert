package kr.concert.infra.concert;

import kr.concert.domain.concert.repo.ConcertRankRepository;
import kr.concert.interfaces.concert.ConcertResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
public class ConcertRankRepositoryImpl implements ConcertRankRepository {

    private final StringRedisTemplate redisTemplate;

    public ConcertRankRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 뒤에 concertId를 붙이고 잔여 seat 갯수를 value 로 넣는다.
    private static final String SEAT_COUNT_KEY = "seat-count:";

    // 뒤에 concertId를 붙이고 오픈 시간 - 매진 시간을 score 로 넣는다.
    private static final String RANK_KEY = "concert-rank";
    private static final String CONCERT_OPEN_DATE_KEY = "concert-open-date:";
    private static final int DEFAULT_SEAT_COUNT = 50;
    private static final int RANK_TTL_DAYS = 7;
    private static final int OPEN_TTL_DAYS = 8;

    // concert 오픈 날짜 추가
    @Override
    public void createConcertOpenDate(Long concertId){
        long timeStamp = System.currentTimeMillis();
        long ttlSeconds = OPEN_TTL_DAYS * 24 * 60 * 60; // OPEN_TTL_DAYS in seconds

        String key = CONCERT_OPEN_DATE_KEY + concertId;
        redisTemplate.opsForValue().set(key, String.valueOf(timeStamp), Duration.ofSeconds(ttlSeconds));
    }

    /**
     * 전체 orchestrator: 좌석 수 감소 + sold-out 시 랭킹 등록
     */
    @Override
    public void decreaseSeatCount(Long concertId, String concertName) {
        Long remaining = decreaseSeatCountOnly(concertId);
        if (remaining != null && remaining == 0) {
            registerSoldOutIfNeeded(concertId, concertName);
        }
    }

    @Override
    public void removeRank(String value) {
        redisTemplate.opsForZSet().remove(RANK_KEY, value);
    }

    // 콘서트 랭킹 조회
    @Override
    public List<ConcertResponse.Rank> getConcertRanks() {
        Set<String> values = redisTemplate.opsForZSet().range(RANK_KEY, 0, -1);

        if (values == null || values.isEmpty()) {
            return List.of();
        }

        List<ConcertResponse.Rank> result = new ArrayList<>();
        int rank = 1;

        for (String value : values) {
            String[] parts = value.split("_");

            try {
                String concertName = parts[1];
                result.add(new ConcertResponse.Rank(rank++, concertName));
            } catch (Exception e) {
                log.warn("getConcertRanks 실패. value: {}", value, e);
            }
        }

        return result;
    }

    @Override
    public Set<String> getRawRankingValues() {
        return redisTemplate.opsForZSet().range(RANK_KEY, 0, -1);
    }

    /**
     * 좌석 수를 1 감소시킵니다. Redis에 키가 없으면 기본값으로 초기화합니다.
     */
    private Long decreaseSeatCountOnly(Long concertId) {
        String key = SEAT_COUNT_KEY + concertId;

        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(DEFAULT_SEAT_COUNT));
        }

        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 좌석이 0이 되었을 때, Redis ZSET 랭킹에 등록합니다.
     */
    private void registerSoldOutIfNeeded(Long concertId, String concertName) {
        String openTimeKey = CONCERT_OPEN_DATE_KEY + concertId;
        String openTimestampStr = redisTemplate.opsForValue().get(openTimeKey);

        if (openTimestampStr != null) {
            try {
                long openTimestamp = Long.parseLong(openTimestampStr);
                long now = System.currentTimeMillis();
                long duration = now - openTimestamp;

                // value 예시: 123_아이유콘서트_20250523
                String value = concertId + "_" + concertName + "_" +
                        LocalDate.now().plusDays(RANK_TTL_DAYS).format(DateTimeFormatter.BASIC_ISO_DATE);

                redisTemplate.opsForZSet().add(RANK_KEY, value, duration);

                // 좌석 수 key 삭제
                redisTemplate.delete(openTimeKey);
            } catch (NumberFormatException e) {
                log.warn("오픈 시간 파싱 실패: {}", openTimestampStr, e);
            }
        }
    }
}
