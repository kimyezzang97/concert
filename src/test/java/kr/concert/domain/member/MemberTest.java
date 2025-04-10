package kr.concert.domain.member;

import kr.concert.interfaces.member.MemberException;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


class MemberTest {

    @Test
    @DisplayName("포인트 작앤은 100만을 초과할 수 없습니다.")
    void canNotChargeTooMuchPoint() {
        // given
        Member member = new Member(1L, "김예찬", 100_000L, LocalDateTime.now(), LocalDateTime.now());
        Long chargePoint = 900_001L;

        // when & then
        assertThatThrownBy(() -> member.chargePoint(chargePoint, LocalDateTime.now()))
                .isInstanceOf(MemberException.CanNotTooMuchChargeException.class)
                .hasMessage("Are you rich?");

    }

    @Test
    @DisplayName("포인트 충전은 0 미만으로 할 수 없습니다.")
    void canNotChargeMinusPoint() {
        // given
        Member member = new Member(1L, "김예찬", 100_000L, LocalDateTime.now(), LocalDateTime.now());
        Long chargePoint = -1L;

        // when & then
        assertThatThrownBy(() -> member.chargePoint(chargePoint, LocalDateTime.now()))
                .isInstanceOf(MemberException.CanNotMinusChargeException.class)
                .hasMessage("Are you kidding me?");
    }

    @Test
    @DisplayName("포인트 충전에 성공한다")
    void chargePointSuccess() {
        // given
        Member member = new Member(1L, "김예찬", 100_000L, LocalDateTime.now(), LocalDateTime.now());
        Long chargePoint = 900_000L;

        // when
        member.chargePoint(chargePoint, LocalDateTime.now());

        // then
        assertThat(member.getPoint()).isEqualTo(1_000_000L);
    }

    @Test
    @DisplayName("포인트 충전 시 수정 날짜가 변경된다")
    void chargePointUpdatesUpdatedAt() {
        // given
        LocalDateTime beforeDate = LocalDateTime.of(2025, 4, 10, 0, 0);
        LocalDateTime afterDate = LocalDateTime.of(2025, 4, 10, 1, 0);
        Member member = new Member(1L, "김예찬", 100_000L, beforeDate, beforeDate);
        Long chargePoint = 900_000L;

        // when
        member.chargePoint(chargePoint, afterDate);

        // then
        assertThat(member.getUpdatedAt()).isEqualTo(afterDate);
    }
}