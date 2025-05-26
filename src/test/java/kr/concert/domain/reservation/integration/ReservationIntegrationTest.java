package kr.concert.domain.reservation.integration;

import kr.concert.TestContainerConfig;
import kr.concert.application.reservation.ReservationFacade;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.repo.QueueRepository;
import kr.concert.domain.reservation.ReservationStatus;
import kr.concert.domain.reservation.event.ReservationEventPublisher;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.schedule.repo.ScheduleRepository;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.repo.SeatRepository;
import kr.concert.interfaces.reservation.ReservationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@SpringBootTest
@Transactional
@Testcontainers
@Import(ReservationIntegrationTest.MockConfig.class)
@ActiveProfiles("test")
public class ReservationIntegrationTest extends TestContainerConfig {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ReservationEventPublisher reservationEventPublisher() {
            return mock(ReservationEventPublisher.class);
        }
    }

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private ReservationEventPublisher reservationEventPublisher;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private QueueRepository queueRepository;

    @Test
    @DisplayName("콘서트 목록 조회")
    void getConcerts() {
        concertRepository.save(new Concert(null, "테스트 콘서트"));

        List<ReservationResponse.GetConcerts> concerts = reservationFacade.getConcerts();

        assertThat(concerts).isNotEmpty();
        assertThat(concerts.get(0).concertName()).isEqualTo("테스트 콘서트");
    }

    @Test
    @DisplayName("스케줄 목록 조회")
    void getSchedulesOfConcert() {
        Concert concert = concertRepository.save(new Concert(null, "스케줄 콘서트"));
        scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));

        List<ReservationResponse.GetScheduleOfConcert> schedules = reservationFacade.getSchedulesOfConcert(concert.getConcertId());

        assertThat(schedules).isNotEmpty();
        assertThat(schedules.get(0).concertName()).isEqualTo("스케줄 콘서트");
    }

    @Test
    @DisplayName("좌석 목록 조회")
    void getSeatsOfSchedule() {
        Concert concert = concertRepository.save(new Concert(null,"좌석 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        seatRepository.save(new Seat(1L, null, schedule, 1L, 3000L, false));

        List<ReservationResponse.GetSeatsOfSchedule> seats = reservationFacade.getSeatsOfSchedule(schedule.getScheduleId());

        assertThat(seats).isNotEmpty();
        assertThat(seats.get(0).seatNumber()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유효한 토큰으로 좌석 예약 성공")
    void reserveSeatSuccess() {
        Member member = memberRepository.save(Member.create( "김예찬", 5000L));
        Concert concert = concertRepository.save(new Concert(null,"예매 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        Seat seat = seatRepository.save(new Seat(1L,null, schedule, 1L, 3000L, true));

        String validToken = UUID.randomUUID().toString();
        Queue queue = Queue.create(member, validToken);
        queue.changeStatusToPlay(LocalDateTime.now().plusMinutes(5));
        queueRepository.save(queue);

        ReservationResponse.Reserve reserve = reservationFacade.reserve(seat.getSeatId(), member.getMemberId(), validToken);

        assertThat(reserve).isNotNull();
        assertThat(reserve.reservationStatus()).isEqualTo(ReservationStatus.TEMP);

        // 이벤트 발행 검증
        verify(reservationEventPublisher).publish( // <-- 여기에러
                argThat(event ->
                        event.getMemberName().equals("김예찬") &&
                                event.getSeatNumber().equals(seat.getSeatNumber()))
        );
    }

    @Test
    @DisplayName("잘못된 토큰으로 좌석 예약 실패")
    void reserveSeatFailWithInvalidToken() {
        Member member = memberRepository.save(Member.create( "고길동", 5000L));
        Concert concert = concertRepository.save(new Concert(null, "토큰 실패 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        Seat seat = seatRepository.save(new Seat(1L,null, schedule, 1L, 3000L, false));

        String wrongToken = UUID.randomUUID().toString();

        assertThatThrownBy(() -> reservationFacade.reserve(seat.getSeatId(), member.getMemberId(), wrongToken))
                .hasMessageContaining("Token Not Exist");
    }
}
