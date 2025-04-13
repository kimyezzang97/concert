package kr.concert.domain.seat;

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
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    @DisplayName("해당 스케줄에 좌석이 없으면 예외가 발생한다.")
    void ifNoSeatsForScheduleCanNotGetList() {
        // given
        Long scheduleId = 1L;
        given(seatRepository.getSeatsOfSchedule(scheduleId)).willReturn(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> seatService.getSeatsOfSchedule(scheduleId))
                .isInstanceOf(ReservationException.SeatNotExistException.class)
                .hasMessage("Seat Not Exists");
    }

    @Test
    @DisplayName("해당 스케줄에 좌석이 존재하면 좌석 리스트를 반환한다.")
    void ifSeatsExistForScheduleCanGetList() {
        // given
        Long scheduleId = 1L;
        List<Seat> seats = List.of(
                new Seat(1L, scheduleId, 1L, 50000L, false, LocalDateTime.now(), LocalDateTime.now())
        );
        given(seatRepository.getSeatsOfSchedule(scheduleId)).willReturn(seats);

        // when
        List<Seat> result = seatService.getSeatsOfSchedule(scheduleId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSeatId()).isEqualTo(1L);
        assertThat(result.get(0).getScheduleId()).isEqualTo(scheduleId);
        assertThat(result.get(0).isSeatStatus()).isEqualTo(false);
    }
}