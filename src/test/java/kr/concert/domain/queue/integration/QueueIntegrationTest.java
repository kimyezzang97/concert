package kr.concert.domain.queue.integration;

import kr.concert.TestContainerConfig;
import kr.concert.application.queue.QueueFacade;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.repo.QueueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class QueueIntegrationTest extends TestContainerConfig {

    @Autowired
    private QueueFacade queueFacade;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private QueueRepository queueRepository;

    @Test
    @DisplayName("회원이 대기열 토큰을 생성할 수 있다.")
    void memberCanCreateQueueToken() {
        // given
        Member member = memberRepository.save(new Member(null, "김예찬", 1000L));

        // when
        String token = queueFacade.createToken(member.getMemberId());

        // then
        Queue queue = queueRepository.findByToken(token).orElseThrow();
        assertThat(queue.getToken()).isEqualTo(token);
        assertThat(queue.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(queue.getQueueStatus().name()).isEqualTo("WAIT");
    }

    @Test
    @DisplayName("존재하지 않는 회원은 대기열 토큰을 생성할 수 없다.")
    void invalidMemberCannotCreateToken() {
        // when & then
        assertThatThrownBy(() -> queueFacade.createToken(9999L))
                .isInstanceOf(kr.concert.interfaces.member.MemberException.MemberNotFoundException.class)
                .hasMessage("Member Not Found");
    }

}
