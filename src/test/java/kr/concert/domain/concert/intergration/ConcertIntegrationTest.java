package kr.concert.domain.concert.intergration;

import kr.concert.TestContainerConfig;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRankRepository;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.concert.service.ConcertService;
import kr.concert.interfaces.concert.ConcertResponse;
import kr.concert.interfaces.reservation.ReservationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class ConcertIntegrationTest extends TestContainerConfig {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertRankRepository concertRankRepository;

    @Test
    @DisplayName("통합테스트: 콘서트가 없으면 예외 발생")
    void getConcerts_EmptyList_ShouldThrowException() {
        // given
        //concertRepository.deleteAll();

        // when & then
        assertThatThrownBy(() -> concertService.getConcerts())
                .isInstanceOf(ReservationException.ConcertNotExistException.class)
                .hasMessage("Concert Not Exists");
    }

    @Test
    @DisplayName("통합테스트: 콘서트가 있으면 리스트 반환")
    void getConcerts_ShouldReturnConcertList() {
        // given
        String concertName = "통합 콘서트 테스트";
        concertService.createConcert(concertName);

        // when
        List<Concert> result = concertService.getConcerts();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConcertName()).isEqualTo("통합 콘서트 테스트");
    }

    @Test
    @DisplayName("통합테스트: 콘서트 생성 시오픈 날짜도 저장된다")
    void createConcert_ShouldPersistToDbAndRedis() {
        // given
        String concertName = "Redis 포함 콘서트";

        // when
        var response = concertService.createConcert(concertName);

        // then
        Concert concert = concertRepository.findById(response.concertId())
                .orElseThrow(() -> new IllegalStateException("Concert not found"));
        assertThat(concert.getConcertName()).isEqualTo(concertName);

        // Redis 검증
        String redisKey = "concert-open-date:" + response.concertId();
        String redisValue = redisTemplate.opsForValue().get(redisKey);

        assertThat(redisValue).isNotNull();
    }

    @Test
    @DisplayName("통합테스트: 콘서트 매진 시 Redis 랭킹에 등록된다")
    void getConcertRanks_WhenSoldOut_ShouldReturnRanking() {
        // given
        String concertName = "Redis 랭킹 테스트";
        var created = concertService.createConcert(concertName);
        Long concertId = created.concertId();

        // Redis 좌석 수를 1로 세팅 (곧 매진 만들기 위해)
        redisTemplate.opsForValue().set("seat-count:" + concertId, "1");

        // when: 좌석 1 감소 → 매진 트리거 → 랭킹 등록
        concertRankRepository.decreaseSeatCount(concertId, concertName);

        // then: 랭킹 목록 조회
        List<ConcertResponse.Rank> ranks = concertService.getConcertRanks();

        assertThat(ranks).isNotEmpty();
        assertThat(ranks.get(0).concertName()).isEqualTo(concertName);
    }

    @Test
    @DisplayName("통합테스트 - 만료된 콘서트 랭킹 스케줄러가 랭킹을 삭제한다")
    void cleanupOldConcertRankings_removesExpiredRankings() {
        // given
        Long concertId = 999L;
        String concertName = "통합 테스트 콘서트";
        LocalDate expiredDate = LocalDate.now().minusDays(1);
        String expiredValue = concertId + "_" + concertName + "_" + expiredDate.format(DateTimeFormatter.BASIC_ISO_DATE);

        redisTemplate.opsForZSet().add("concert-rank", expiredValue, 1000L);

        // when
        concertRankRepository.removeRank(expiredValue);

        // then
        Set<String> valuesAfterCleanup = concertRankRepository.getRawRankingValues();
        assertThat(valuesAfterCleanup).doesNotContain(expiredValue);
    }
}
