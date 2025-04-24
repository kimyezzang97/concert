package kr.concert.domain.reservation;

import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.seat.entity.Seat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    @DisplayName("예약 생성에 성공합니다.")
    void createReservation_success() {
        // given
        Member member = new Member(1L, "김예찬", 1000L);
        Seat seat = new Seat(1L, new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now())
                ,1L, 1000L, true);
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        // when
        Reservation reservation = Reservation.create(member, seat, expiredAt);

        // then
        assertThat(reservation.getMember()).isEqualTo(member);
        assertThat(reservation.getSeat()).isEqualTo(seat);
        assertThat(reservation.getReservationStatus().name()).isEqualTo("TEMP");
        assertThat(reservation.getExpiredAt()).isEqualTo(expiredAt);
    }

    @Test
    @DisplayName("회원 정보가 없으면 예외가 발생합니다.")
    void createReservation_nullMember() {
        Seat seat = new Seat(1L, new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now())
                ,1L, 1000L, true);
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        assertThatThrownBy(() -> Reservation.create(null, seat, expiredAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원 정보는 필수입니다.");
    }

    @Test
    @DisplayName("좌석 정보가 없으면 예외가 발생합니다.")
    void createReservation_nullSeat() {
        Member member = new Member(1L, "김예찬", 1000L);
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        assertThatThrownBy(() -> Reservation.create(member, null, expiredAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("좌석 정보는 필수입니다.");
    }

    @Test
    @DisplayName("만료 시간이 현재 시간보다 이전이면 예외가 발생합니다.")
    void createReservation_expiredTimeInvalid() {
        Member member = new Member(1L, "김예찬", 1000L);
        Seat seat = new Seat(1L, new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now())
                ,1L, 1000L, true);
        LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(5);

        assertThatThrownBy(() -> Reservation.create(member, seat, expiredAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("만료 시간이 현재 시간보다 이전일 수 없습니다.");
    }
}