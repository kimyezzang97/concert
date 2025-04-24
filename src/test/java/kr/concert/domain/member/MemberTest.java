package kr.concert.domain.member;

import kr.concert.domain.member.entity.Member;
import kr.concert.interfaces.member.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {

    @Test
    @DisplayName("포인트 작앤은 100만을 초과할 수 없습니다.")
    void canNotChargeTooMuchPoint() {
        // given
        Member member = Member.create( "김예찬", 100_000L);
        Long chargePoint = 900_001L;

        // when & then
        assertThatThrownBy(() -> member.chargePoint(chargePoint))
                .isInstanceOf(MemberException.CanNotTooMuchChargeException.class)
                .hasMessage("Are you rich?");
    }

    @Test
    @DisplayName("포인트 충전은 0 미만으로 할 수 없습니다.")
    void canNotChargeMinusPoint() {
        // given
        Member member = Member.create( "김예찬", 100_000L);
        Long chargePoint = -1L;

        // when & then
        assertThatThrownBy(() -> member.chargePoint(chargePoint))
                .isInstanceOf(MemberException.CanNotMinusChargeException.class)
                .hasMessage("You can't charge 0 or less!");
 }

    @Test
    @DisplayName("포인트 충전에 성공한다")
    void chargePointSuccess() {
        // given
        Member member = Member.create( "김예찬", 100_000L);
        Long chargePoint = 900_000L;

        // when
        member.chargePoint(chargePoint);

        // then
        assertThat(member.getPoint()).isEqualTo(1_000_000L);
    }

}