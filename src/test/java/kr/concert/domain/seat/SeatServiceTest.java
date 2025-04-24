package kr.concert.domain.seat;

import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.repo.SeatRepository;
import kr.concert.domain.seat.service.SeatService;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        given(seatRepository.findAllBySchedule_ScheduleId(scheduleId)).willReturn(Collections.emptyList());

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
                new Seat(1L, 1L, new Schedule(1L, new Concert(1L, "콜드플레이 콘서트"), LocalDateTime.now()), 1L, 50000L, false)
        );
        given(seatRepository.findAllBySchedule_ScheduleId(scheduleId)).willReturn(seats);

        // when
        List<Seat> result = seatService.getSeatsOfSchedule(scheduleId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSeatId()).isEqualTo(1L);
        assertThat(result.get(0).getSchedule().getScheduleId()).isEqualTo(scheduleId);
        assertThat(result.get(0).isSeatStatus()).isEqualTo(false);
    }

    @Test
    @DisplayName("좌석이 존재하고 예약 가능할 경우 예약을 성공한다.")
    void reserveSeat_success() {
        // given
        Seat seat = new Seat(1L,1L, new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now())
        ,1L, 1000L, true);
        when(seatRepository.findBySeatId(1L)).thenReturn(Optional.of(seat));

        // when
        seatService.reserveSeat(1L);

        // then
        assertThat(seat.isSeatStatus()).isFalse();
        verify(seatRepository).findBySeatId(1L);
    }

    @Test
    @DisplayName("좌석이 존재하지 않으면 예외가 발생한다")
    void reserveSeat_seatNotFound() {
        when(seatRepository.findBySeatId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seatService.reserveSeat(1L))
                .isInstanceOf(ReservationException.SeatNotExistException.class);
    }

    @Test
    @DisplayName("이미 예약된 좌석이면 예외가 발생한다.")
    void reserveSeat_alreadyReserved() {
        Seat seat = new Seat(1L, 1L, new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now())
                ,1L, 1000L, false);
        when(seatRepository.findBySeatId(1L)).thenReturn(Optional.of(seat));

        assertThatThrownBy(() -> seatService.reserveSeat(1L))
                .isInstanceOf(ReservationException.SeatImpossibleException.class);
    }

    @Test
    @DisplayName("좌석 조회 성공한다.")
    void getSeat_success() {
        Seat seat = new Seat(1L, 1L, new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now()), 101L, 50000L, true);
        when(seatRepository.findBySeatId(1L)).thenReturn(Optional.of(seat));

        Seat result = seatService.getSeat(1L);

        assertThat(result).isEqualTo(seat);
    }

    @Test
    @DisplayName("좌석이 존재하지 않으면 예외가 발생한다.")
    void getSeat_seatNotFound() {
        when(seatRepository.findBySeatId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seatService.getSeat(1L))
                .isInstanceOf(ReservationException.SeatNotExistException.class);
    }
}