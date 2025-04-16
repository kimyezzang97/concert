package kr.concert.domain.queue;

import kr.concert.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}