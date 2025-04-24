package kr.concert.domain.payment.service;

import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.payment.entity.Payment;
import kr.concert.domain.payment.repo.PaymentRepository;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.interfaces.payment.PaymentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("정상적인 정보로 결제를 생성하고 저장하며 응답을 반환한다.")
    void createPayment_success() {
        // given
        Member member = new Member(1L, 1L, "user@test.com", 1000L);
        Seat seat = new Seat(1L, new Schedule(1L, new Concert(1L, "그린데이 콘서트"), LocalDateTime.now().plusDays(1)), 1L, 1000L, true);
        Reservation reservation = Reservation.create(member, seat, LocalDateTime.now().plusDays(1));
        Long price = 30000L;

        Payment savedPayment = new Payment(100L, reservation, member, price);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // when
        PaymentResponse.payment response = paymentService.createPayment(reservation, member, price);

        // then
        assertThat(response).isNotNull();
        assertThat(response.paymentId()).isEqualTo(100L);
        assertThat(response.memberId()).isEqualTo(1L);
        assertThat(response.price()).isEqualTo(price);
    }
}