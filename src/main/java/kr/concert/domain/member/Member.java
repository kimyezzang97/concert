package kr.concert.domain.member;

import kr.concert.interfaces.member.MemberException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Member {

    private Long memberId;
    private String memberName;
    private Long point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final Long MAX_POINT = 1_000_000L;

    // 포인트를 충전한다.
    public void chargePoint(Long amount, LocalDateTime pointUpdatedAt) {
        if (amount < 0) throw new MemberException.CanNotMinusChargeException();

        long totalPoint = point + amount;
        if (totalPoint > MAX_POINT) throw new MemberException.CanNotTooMuchChargeException();

        this.point = totalPoint;
        this.updatedAt = pointUpdatedAt;
    }


}
