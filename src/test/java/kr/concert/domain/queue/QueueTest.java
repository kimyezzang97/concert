package kr.concert.domain.queue;

import kr.concert.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    @Test
    @DisplayName("대기열 토큰 생성을 성공합니다.")
    void createQueueTest() {
        // given
        Member member = new Member(1L, "김예찬", 1000L);
        String token = UUID.randomUUID().toString();

        // when
        Queue queue = Queue.create(member, token);

        // then
        assertThat(queue.getMember()).isEqualTo(member);
        assertThat(queue.getToken()).isEqualTo(token);
        assertThat(queue.getQueueStatus()).isEqualTo(QueueStatus.WAIT);
        assertThat(queue.getExpiredAt()).isNull(); // 생성 시점엔 null
    }

    @Test
    @DisplayName("회원 정보가 null이므로 대기열 토큰 생성을 실패합니다.")
    void ifMemberIsNullCanNotCreateQueueToken() {
        String token = "sample-token";
        assertThatThrownBy(() -> Queue.create(null, token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원 정보는 필수입니다.");
    }

    @Test
    @DisplayName("토큰 정보가 null이므로 대기열 토큰 생성을 실패합니다.")
    void ifMemberTokenNamesNullCanNotCreateQueueToken() {
        Member member = new Member(1L, "김예찬", 1000L);

        assertThatThrownBy(() -> Queue.create(member, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토큰은 null 또는 빈 문자열일 수 없습니다.");
    }

    @Test
    @DisplayName("토큰 정보가 빈 문자열이므로 대기열 토큰 생성을 실패합니다.")
    void ifMemberTokenNamesEmptyCanNotCreateQueueToken() {
        Member member = new Member(1L, "김예찬", 1000L);

        assertThatThrownBy(() -> Queue.create(member, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토큰은 null 또는 빈 문자열일 수 없습니다.");
    }

    @Test
    @DisplayName("Queue 상태를 PLAY 로 변경합니다.")
    void changeStatusToPlay_updatesQueueStatus() {
        // given
        Member member = new Member(1L, "테스트회원", 1000L);
        String token = "sample-token";

        Queue queue = Queue.create(member, token);
        assertThat(queue.getQueueStatus()).isEqualTo(QueueStatus.WAIT); // 초기 상태 확인

        // when
        queue.changeStatusToPlay();

        // then
        assertThat(queue.getQueueStatus()).isEqualTo(QueueStatus.PLAY);
    }
}