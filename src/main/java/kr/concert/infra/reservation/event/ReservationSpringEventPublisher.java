package kr.concert.infra.reservation.event;

import kr.concert.domain.reservation.event.ReservationEvent;
import kr.concert.domain.reservation.event.ReservationEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ReservationSpringEventPublisher implements ReservationEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public ReservationSpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(ReservationEvent reservationEvent) {
        applicationEventPublisher.publishEvent(reservationEvent);
    }
}
