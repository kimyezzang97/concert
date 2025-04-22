package kr.concert.domain.seat.integration;

import kr.concert.TestContainerConfig;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.schedule.repo.ScheduleRepository;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.repo.SeatRepository;
import kr.concert.domain.seat.service.SeatService;
import kr.concert.interfaces.reservation.ReservationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class SeatIntegrationTest extends TestContainerConfig {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    @DisplayName("해당 스케줄에 좌석이 없으면 예외가 발생한다.")
    void noSeats_throwException() {
        Concert concert = concertRepository.save(new Concert(null, "빈 좌석 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));

        assertThatThrownBy(() -> seatService.getSeatsOfSchedule(schedule.getScheduleId()))
                .isInstanceOf(ReservationException.SeatNotExistException.class)
                .hasMessage("Seat Not Exists");
    }

    @Test
    @DisplayName("해당 스케줄에 좌석이 있으면 리스트를 반환한다.")
    void seatsExist_returnList() {
        Concert concert = concertRepository.save(new Concert(null, "좌석 존재 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        Seat savedSeat = seatRepository.save(new Seat(null, schedule, 1L, 50000L, true));

        List<Seat> seats = seatService.getSeatsOfSchedule(schedule.getScheduleId());

        assertThat(seats).hasSize(1);
        assertThat(seats.get(0).getSeatId()).isEqualTo(savedSeat.getSeatId());
    }

    @Test
    @DisplayName("예약 가능한 좌석이면 예약을 성공한다.")
    void reserveSeat_success() {
        Concert concert = concertRepository.save(new Concert(null, "예약 성공 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        Seat seat = seatRepository.save(new Seat(null, schedule, 1L, 10000L, true));

        seatService.reserveSeat(seat.getSeatId());

        Seat reservedSeat = seatRepository.findBySeatId(seat.getSeatId()).get();
        assertThat(reservedSeat.isSeatStatus()).isFalse();
    }

    @Test
    @DisplayName("예약하려는 좌석이 없으면 예외가 발생한다.")
    void reserveSeat_seatNotExist() {
        assertThatThrownBy(() -> seatService.reserveSeat(999L))
                .isInstanceOf(ReservationException.SeatNotExistException.class);
    }

    @Test
    @DisplayName("이미 예약된 좌석이면 예외가 발생한다.")
    void reserveSeat_alreadyReserved() {
        Concert concert = concertRepository.save(new Concert(null, "이미 예약 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        Seat seat = seatRepository.save(new Seat(null, schedule, 1L, 20000L, false));

        assertThatThrownBy(() -> seatService.reserveSeat(seat.getSeatId()))
                .isInstanceOf(ReservationException.SeatImpossibleException.class);
    }

    @Test
    @DisplayName("좌석 조회에 성공한다.")
    void getSeat_success() {
        Concert concert = concertRepository.save(new Concert(null, "조회 콘서트"));
        Schedule schedule = scheduleRepository.save(new Schedule(null, concert, LocalDateTime.now().plusDays(1)));
        Seat seat = seatRepository.save(new Seat(null, schedule, 101L, 15000L, true));

        Seat result = seatService.getSeat(seat.getSeatId());

        assertThat(result.getSeatId()).isEqualTo(seat.getSeatId());
        assertThat(result.getSchedule().getScheduleId()).isEqualTo(schedule.getScheduleId());
    }

    @Test
    @DisplayName("좌석 조회 시 좌석이 없으면 예외가 발생한다.")
    void getSeat_notExist() {
        assertThatThrownBy(() -> seatService.getSeat(1234L))
                .isInstanceOf(ReservationException.SeatNotExistException.class);
    }
}
