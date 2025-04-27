package kr.concert.domain.payment.integration;

import kr.concert.TestContainerConfig;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
import kr.concert.domain.payment.entity.Payment;
import kr.concert.domain.payment.repo.PaymentRepository;
import kr.concert.domain.payment.service.PaymentService;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.reservation.repo.ReservationRepository;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.schedule.repo.ScheduleRepository;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.repo.SeatRepository;
import kr.concert.interfaces.payment.PaymentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class PaymentIntegrationTest extends TestContainerConfig {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제 통합 테스트 - 정상적인 정보로 결제를 생성하고 저장하며 응답을 반환한다.")
    void createPayment_success() {
        // given
        Member member = memberRepository.save(Member.create( "user@test.com", 1000L));
        Concert concert = concertRepository.save(new Concert(null, "그린데이 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        Seat seat = seatRepository.save(new Seat(1L, null, schedule, 1L, 1000L, true));
        Reservation reservation = reservationRepository.save(Reservation.create(member, seat, LocalDateTime.now().plusDays(1)));

        Long price = 30000L;

        // when
        PaymentResponse.payment response = paymentService.createPayment(reservation, member, price);

        // then
        assertThat(response).isNotNull();
        assertThat(response.paymentId()).isNotNull();
        assertThat(response.memberId()).isEqualTo(member.getMemberId());
        assertThat(response.price()).isEqualTo(price);
    }
}
