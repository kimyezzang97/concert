package kr.concert.domain.concert;


import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRankRepository;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.concert.service.ConcertService;
import kr.concert.interfaces.concert.ConcertResponse;
import kr.concert.interfaces.reservation.ReservationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private ConcertRankRepository concertRankRepository;

    @InjectMocks
    private ConcertService concertService;

    @Test
    @DisplayName("등록된 콘서트가 없으면 예외가 발생한다.")
    void ifNotExistConcertsCanNotGetList() {
        // given
        given(concertRepository.findAll()).willReturn(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> concertService.getConcerts())
                .isInstanceOf(ReservationException.ConcertNotExistException.class)
                .hasMessage("Concert Not Exists");
    }

    @Test
    @DisplayName("콘서트가 존재하면 콘서트 리스트를 반환한다.")
    void ifExistConcertsCanGetList() {
        // given
        List<Concert> concerts = List.of(
                new Concert(1L, "뮤직 페스티벌")
        );
        given(concertRepository.findAll()).willReturn(concerts);

        // when
        List<Concert> result = concertService.getConcerts();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConcertId()).isEqualTo(1L);
        assertThat(result.get(0).getConcertName()).isEqualTo("뮤직 페스티벌");
    }

    @Test
    @DisplayName("Redis에서 랭킹 리스트 조회 성공")
    void testGetConcertRanks() {
        // given
        List<ConcertResponse.Rank> dummyRanks = List.of(
                new ConcertResponse.Rank(1, "아이유"),
                new ConcertResponse.Rank(2, "뉴진스")
        );
        when(concertRankRepository.getConcertRanks()).thenReturn(dummyRanks);

        // when
        List<ConcertResponse.Rank> result = concertService.getConcertRanks();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).concertName()).isEqualTo("아이유");
        verify(concertRankRepository).getConcertRanks();
    }

    @Test
    @DisplayName("콘서트 목록 정상 반환")
    void testGetConcerts() {
        // given
        List<Concert> concerts = List.of(new Concert(1L, "IU Concert"));
        when(concertRepository.findAll()).thenReturn(concerts);

        // when
        List<Concert> result = concertService.getConcerts();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getConcertName()).isEqualTo("IU Concert");
    }
}