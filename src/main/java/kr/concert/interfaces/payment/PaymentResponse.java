package kr.concert.interfaces.payment;

public class PaymentResponse {

    public record payment(Long paymentId, Long memberId, Long price) {}
}
