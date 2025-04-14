package kr.concert.domain.member;

import jakarta.persistence.*;
import kr.concert.domain.BaseEntity;
import kr.concert.interfaces.member.MemberException;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity(name="member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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
    public void chargePoint(Long amount, LocalDateTime pointUpdatedAt) {
        if (amount < 0) throw new MemberException.CanNotMinusChargeException();

        long totalPoint = point + amount;
        if (totalPoint > MAX_POINT) throw new MemberException.CanNotTooMuchChargeException();

        this.point = totalPoint;
    }

}
