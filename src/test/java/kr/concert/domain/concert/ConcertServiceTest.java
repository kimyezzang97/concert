package kr.concert.domain.concert;


import kr.concert.interfaces.member.MemberException;
import kr.concert.interfaces.reservation.ReservationException;
import kr.concert.interfaces.reservation.ReservationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

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
}