package kr.concert.domain.schedule;

import kr.concert.interfaces.reservation.ReservationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("해당 콘서트에 스케줄이 없으면 예외가 발생한다.")
    void ifNoSchedulesForConcertCanNotGetList() {
        // given
        Long concertId = 1L;
        given(scheduleRepository.getSchedulesOfConcert(concertId)).willReturn(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> scheduleService.getSchedulesOfConcert(concertId))
                .isInstanceOf(ReservationException.ScheduleNotExistException.class)
                .hasMessage("Schedule Not Exists");
    }

    @Test
    @DisplayName("해당 콘서트에 스케줄이 존재하면 스케줄 리스트를 반환한다.")
    void ifSchedulesExistForConcertCanGetList() {
        // given
        Long concertId = 1L;
        List<Schedule> schedules = List.of(
                new Schedule(1L, concertId, LocalDateTime.of(2025, 5, 1, 19, 0), LocalDateTime.now(), LocalDateTime.now())
        );
        given(scheduleRepository.getSchedulesOfConcert(concertId)).willReturn(schedules);

        // when
        List<Schedule> result = scheduleService.getSchedulesOfConcert(concertId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScheduleId()).isEqualTo(1L);
        assertThat(result.get(0).getConcertId()).isEqualTo(concertId);
    }
}