package kr.concert.domain.member.entity;

import jakarta.persistence.*;
import kr.concert.domain.BaseEntity;
import kr.concert.interfaces.member.MemberException;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="member")
public class Member extends BaseEntity {

    private static final Long MAX_POINT = 1_000_000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id" ,nullable = false)
    private Long memberId;

    // 회원 이름
    @Column(name = "member_name", nullable = false)
    private String memberName;

    // 포인트
    @Column(name = "point", nullable = false)
    private Long point;

    // 포인트를 충전한다.
    public void chargePoint(Long amount) {
        if (amount < 0) throw new MemberException.CanNotMinusChargeException();

        long totalPoint = point + amount;
        if (totalPoint > MAX_POINT) throw new MemberException.CanNotTooMuchChargeException();

        this.point = totalPoint;
    }

    public void paymentPoint(Long amount){
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("차감할 포인트는 0보다 커야 합니다.");
        }
        if (this.point < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        this.point -= amount;
    }
}
