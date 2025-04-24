package kr.concert.domain.concert.intergration;

import kr.concert.TestContainerConfig;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.concert.service.ConcertService;
import kr.concert.interfaces.reservation.ReservationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class ConcertIntegrationTest extends TestContainerConfig {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertRepository concertRepository;

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
        Concert concert = new Concert(null, "통합 콘서트 테스트");
        concertService.createConcert(concert);

        // when
        List<Concert> result = concertService.getConcerts();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConcertName()).isEqualTo("통합 콘서트 테스트");
    }
}
