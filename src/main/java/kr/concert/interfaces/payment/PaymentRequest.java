package kr.concert.interfaces.payment;

public class PaymentRequest {

    public record payment(Long reservationId, Long memberId, Long price) {}
}
