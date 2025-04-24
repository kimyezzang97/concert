package kr.concert.domain.member.integration;

import jakarta.persistence.EntityManager;
import kr.concert.TestContainerConfig;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
import kr.concert.domain.member.service.MemberService;
import kr.concert.interfaces.member.MemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class MemberIntegrationTest extends TestContainerConfig {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("존재하지 않는 회원은 포인트를 조회할 수 없다.")
    void ifMemberNotExistCanNotGetPoint() {
        // given
        Long nonExistentId = 999L;

        // when & then
        assertThatThrownBy(() -> memberService.getPoint(nonExistentId))
                .isInstanceOf(MemberException.MemberNotFoundException.class)
                .hasMessage("Member Not Found");
    }

    @Test
    @DisplayName("존재하는 회원의 포인트를 조회한다.")
    void ifMemberExistCanGetPoint() {
        // given
        Member member = memberRepository.save(new Member(null, "김예찬", 1000L));

        // when

        // then
        assertThat(member.getPoint()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("존재하지 않는 회원은 포인트를 충전할 수 없다.")
    void ifMemberNotExistCanNotChargePoint() {
        // given
        Long nonExistentId = 999L;

        // when & then
        assertThatThrownBy(() -> memberService.chargePoint(nonExistentId, 1000L))
                .isInstanceOf(MemberException.MemberNotFoundException.class)
                .hasMessage("Member Not Found");
    }

    @Test
    @DisplayName("존재하는 회원은 포인트를 충전할 수 있다.")
    void ifMemberExistCanChargePoint() {
        // given
        Member member = memberRepository.save(new Member(null, "김예찬", 1000L));

        // when
        memberService.chargePoint(member.getMemberId(), 500L);
        Member updatedMember = memberRepository.findById(member.getMemberId()).orElseThrow();

        // then
        assertThat(updatedMember.getPoint()).isEqualTo(1500L);
    }
}
