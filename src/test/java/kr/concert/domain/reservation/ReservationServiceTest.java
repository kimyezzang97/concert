package kr.concert.domain.reservation;

import kr.concert.domain.concert.Concert;
import kr.concert.domain.member.Member;
import kr.concert.domain.schedule.Schedule;
import kr.concert.domain.seat.Seat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("정상적으로 예약을 생성합니다.")
    void createReservation_success() {
        // given
        Member member = new Member(1L, "김예찬", 1000L);
        Seat seat = new Seat(1L, new Schedule(1L, new Concert(1L, "카라 콘서트"), LocalDateTime.now()), 1L, 50000L, true);

        Reservation fakeReservation = Reservation.create(member, seat, LocalDateTime.now().plusDays(1));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(fakeReservation);

        // when
        Reservation result = reservationService.createReservation(member, seat);

        // then
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getSeat()).isEqualTo(seat);
        assertThat(result.getReservationStatus().name()).isEqualTo("TEMP");
    }
}