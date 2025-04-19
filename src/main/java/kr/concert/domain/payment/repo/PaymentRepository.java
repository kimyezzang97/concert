package kr.concert.domain.payment.repo;

import kr.concert.domain.payment.entity.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
}
