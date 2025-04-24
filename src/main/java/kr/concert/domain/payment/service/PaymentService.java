package kr.concert.domain.payment.service;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.payment.entity.Payment;
import kr.concert.domain.payment.repo.PaymentRepository;
import kr.concert.domain.reservation.entity.Reservation;
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

        Payment savendPayment = paymentRepository.save(payment);

        return new PaymentResponse.payment(savendPayment.getPaymentId(), member.getMemberId(), price);
    }
}
