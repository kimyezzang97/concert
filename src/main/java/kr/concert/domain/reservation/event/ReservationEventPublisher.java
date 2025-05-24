package kr.concert.domain.reservation.event;


public interface ReservationEventPublisher {

    void publish(ReservationEvent reservationEvent);
}
