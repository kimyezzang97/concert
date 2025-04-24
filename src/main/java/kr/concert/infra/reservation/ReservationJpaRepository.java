package kr.concert.infra.reservation;

import kr.concert.domain.reservation.ReservationStatus;
import kr.concert.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByReservationStatusAndExpiredAtBefore(ReservationStatus status, LocalDateTime expiredAt);
}
