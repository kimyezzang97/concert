package kr.concert.domain.reservation;

import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.reservation.repo.ReservationRepository;
import kr.concert.domain.reservation.service.ReservationService;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.seat.entity.Seat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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
        Member member = Member.create( "김예찬", 1000L);
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

    @Test
    @DisplayName("TEMP 상태의 만료된 예약들을 취소하고 좌석 리스트를 반환한다.")
    void cancelReservation_shouldExpireReservationsAndReturnSeats() {
        // given
        Seat seat1 = mock(Seat.class);
        Seat seat2 = mock(Seat.class);

        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);

        when(reservation1.getSeat()).thenReturn(seat1);
        when(reservation2.getSeat()).thenReturn(seat2);

        List<Reservation> expiredReservations = List.of(reservation1, reservation2);
        when(reservationRepository.getExpireReservations(eq(ReservationStatus.TEMP), any(LocalDateTime.class)))
                .thenReturn(expiredReservations);

        // when
        List<Seat> result = reservationService.cancelReservation();

        // then
        assertThat(result).containsExactly(seat1, seat2);
    }
}