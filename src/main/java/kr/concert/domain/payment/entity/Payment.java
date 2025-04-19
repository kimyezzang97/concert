package kr.concert.domain.payment.entity;

import jakarta.persistence.*;
import kr.concert.domain.BaseEntity;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id" ,nullable = false)
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 관리, DB에서 진짜 필요한 것만 쿼리로 날림.
    @JoinColumn(name = "reservation_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK 제약 X
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 관리, DB에서 진짜 필요한 것만 쿼리로 날림.
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK 제약 X
    private Member member;

    @Column(name = "payment_price", nullable = false)
    private Long paymentPrice;

    public static Payment create(Reservation reservation, Member member, Long paymentPrice) {
        if (member == null) throw new IllegalArgumentException("회원 정보는 필수입니다.");
        if (reservation == null) throw new IllegalArgumentException("예약 정보는 필수입니다.");
        if (paymentPrice == null || paymentPrice <= 0) throw new IllegalArgumentException("결제 금액은 양수여야 합니다.");

        Payment payment = new Payment();
        payment.reservation = reservation;
        payment.member = member;
        payment.paymentPrice = paymentPrice;
        return payment;
    }
}
