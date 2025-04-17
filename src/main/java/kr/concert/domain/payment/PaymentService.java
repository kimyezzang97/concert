package kr.concert.domain.payment;

import kr.concert.domain.member.Member;
import kr.concert.domain.reservation.Reservation;
import kr.concert.interfaces.payment.PaymentRequest;
import kr.concert.interfaces.payment.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse.payment createPayment(Reservation reservation, Member member, Long price) {
        Payment payment = Payment.create(reservation, member, price);

        paymentRepository.save(payment);

        return new PaymentResponse.payment(payment.getPaymentId(), member.getMemberId(), price);
    }
}
