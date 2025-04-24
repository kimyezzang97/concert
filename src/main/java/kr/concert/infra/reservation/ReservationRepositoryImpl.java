package kr.concert.infra.reservation;

import kr.concert.domain.reservation.ReservationStatus;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.reservation.repo.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationRepositoryImpl(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public Optional<Reservation> getReservation(Long reservationId) {
        return reservationJpaRepository.findById(reservationId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public List<Reservation> getExpireReservations(ReservationStatus status, LocalDateTime now) {
        return reservationJpaRepository.findAllByReservationStatusAndExpiredAtBefore(status, now);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationJpaRepository.findAll();
    }
}
