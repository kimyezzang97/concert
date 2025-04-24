package kr.concert.domain.payment.entity;

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
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    @DisplayName("정상적인 정보로 Payment를 생성할 수 있다.")
    void createPayment_success() {
        Member member = Member.create( "user@test.com", 1000L);
        Reservation reservation =
                Reservation.create(member, new Seat(1L, new Schedule(1L, new Concert(1L, "그린데이 콘서트"),LocalDateTime.now()), 1L, 1000L, true), LocalDateTime.now());


        Payment payment = Payment.create(reservation, member, 1000L);

        assertThat(payment.getMember()).isEqualTo(member);
        assertThat(payment.getReservation()).isEqualTo(reservation);
        assertThat(payment.getPaymentPrice()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("회원 정보가 null이면 예외가 발생한다.")
    void createPayment_nullMember() {
        Member member = Member.create( "user@test.com", 1000L);
        Reservation reservation =
                Reservation.create(member, new Seat(1L, new Schedule(1L, new Concert(1L, "그린데이 콘서트"),LocalDateTime.now()), 1L, 1000L, true), LocalDateTime.now());

        assertThatThrownBy(() -> Payment.create(reservation, null, 1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원 정보는 필수입니다.");
    }

    @Test
    @DisplayName("예약 정보가 null이면 예외가 발생한다.")
    void createPayment_nullReservation() {
        Member member = Member.create( "user@test.com", 1000L);

        assertThatThrownBy(() -> Payment.create(null, member, 1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 정보는 필수입니다.");
    }

    @Test
    @DisplayName("결제 금액이 null이거나 0 이하이면 예외가 발생한다.")
    void createPayment_invalidPrice() {
        Member member = Member.create( "user@test.com", 1000L);
        Reservation reservation =
                Reservation.create(member, new Seat(1L, new Schedule(1L, new Concert(1L, "그린데이 콘서트"),LocalDateTime.now().plusDays(1)), 1L, 1000L, true), LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> Payment.create(reservation, member, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 금액은 양수여야 합니다.");

        assertThatThrownBy(() -> Payment.create(reservation, member, 0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 금액은 양수여야 합니다.");

        assertThatThrownBy(() -> Payment.create(reservation, member, -100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 금액은 양수여야 합니다.");
    }
}