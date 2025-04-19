package kr.concert.domain.queue;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.repo.QueueRepository;
import kr.concert.domain.queue.service.QueueService;
import kr.concert.interfaces.queue.QueueException;
import kr.concert.interfaces.queue.QueueResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    @Test
    @DisplayName("대기열 토큰 생성에 성공합니다.")
    void createQueueToken() {
        // given
        Member member = new Member(1L, "김예찬", 1000L);
        String expectedToken = UUID.randomUUID().toString();

        Queue savedQueue = mock(Queue.class);
        when(savedQueue.getToken()).thenReturn(expectedToken);
        when(queueRepository.save(any(Queue.class))).thenReturn(savedQueue);

        // when
        String result = queueService.createToken(member);

        // then
        assertThat(result).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("회원정보가 없으면 대기열 토큰 생성에 실패합니다.")
    void ifMemberIsNullCanNotCreateQueueToken() {
        // given
        Member member = null; // 회원이 null인 경우

        // when & then
        // createToken 메서드를 실행할 때 IllegalArgumentException이 발생하는지 확인
        assertThatThrownBy(() -> queueService.createToken(member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원 정보는 필수입니다."); // 예외 메시지까지 검증
    }

    @DisplayName("존재하지 않는 토큰이면 예외를 던집니다.")
    @Test
    void getQueueStatusWithInvalidTokenThrowsException() {
        // given
        String invalidToken = UUID.randomUUID().toString();
        when(queueRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> queueService.getQueue(invalidToken))
                .isInstanceOf(QueueException.TokenNotExistException.class)
                .hasMessage("Token Not Exists");
    }

    @DisplayName("유효한 토큰으로 대기열 정보를 조회합니다.")
    @Test
    void getQueueStatusWithValidToken() {
        // given
        Member member = new Member(1L, "김예찬", 1000L);
        String token = UUID.randomUUID().toString();

        Queue queue = new Queue(
                5L, // queueId
                member,
                token,
                QueueStatus.WAIT,
                null
        );

        when(queueRepository.findByToken(token)).thenReturn(Optional.of(queue));
        when(queueRepository.countByQueueStatusAndQueueIdLessThan(QueueStatus.WAIT, 5L)).thenReturn(4L);

        // when
        QueueResponse.QueueStatus result = queueService.getQueue(token);

        // then
        assertThat(result.status()).isEqualTo("WAIT");
        assertThat(result.position()).isEqualTo(5); // 4명 앞 + 자기 자신
    }

    @Test
    @DisplayName("토큰과 회원 ID가 유효하고 상태가 PLAY면 검증이 성공합니다.")
    void checkTokenSuccess() {
        // given
        String token = "sample-token";
        Long memberId = 1L;

        Member member = new Member(memberId, "테스트유저", 1000L);
        Queue queue = Queue.create(member, token);
        queue.changeStatusToPlay(); // 상태 PLAY로 변경

        when(queueRepository.findByTokenAndMember_MemberId(token, memberId)).thenReturn(Optional.of(queue));

        // when & then
        assertDoesNotThrow(() -> queueService.checkToken(token, memberId));
    }

    @Test
    @DisplayName("토큰과 회원 ID에 해당하는 대기열이 없으면 예외가 발생합니다.")
    void ifNotExistTokenThrowsException() {
        // given
        String token = "invalid-token";
        Long memberId = 2L;

        when(queueRepository.findByTokenAndMember_MemberId(token, memberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> queueService.checkToken(token, memberId))
                .isInstanceOf(QueueException.TokenNotExistException.class);
    }

    @Test
    @DisplayName("대기열 상태가 PLAY가 아니면 예외가 발생합니다.")
    void ifNotPlayTokenThrowsException() {
        // given
        String token = "not-play-token";
        Long memberId = 3L;

        Member member = new Member(memberId, "테스트유저2", 2000L);
        Queue queue = Queue.create(member, token); // 기본 상태는 WAIT

        when(queueRepository.findByTokenAndMember_MemberId(token, memberId)).thenReturn(Optional.of(queue));

        // when & then
        assertThatThrownBy(() -> queueService.checkToken(token, memberId))
                .isInstanceOf(QueueException.TokenNotPlayException.class);
    }

}