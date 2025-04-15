package kr.concert.interfaces.reservation;

import java.time.LocalDateTime;

public class ReservationRequest {

    public record Reservation(Long memberId) {}
}
