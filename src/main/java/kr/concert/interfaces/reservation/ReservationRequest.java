package kr.concert.interfaces.reservation;


public class ReservationRequest {

    public record Reserve(Long memberId, String token, Long seatId) {}
}
