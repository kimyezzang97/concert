package kr.concert.domain.seat;

import kr.concert.domain.concert.Concert;
import kr.concert.domain.schedule.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    @DisplayName("좌석 예약 시 상태가 IMPOSSIBLE(false)로 변경된다")
    void reserveSeatStatus_shouldSetStatusToFalse() {
        // given
        Schedule schedule = new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now()); // 실제 테스트에서는 Stub이나 Builder 사용 가능
        Seat seat = new Seat(
                1L,
                schedule,
                101L,
                50000L,
                true // 초기 상태는 가능 (POSSIBLE)
        );

        // when
        seat.reserveSeatStatus();

        // then
        assertThat(seat.isSeatStatus()).isFalse(); // 예약 불가 상태로 바뀌었는지 확인
    }
}