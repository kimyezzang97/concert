package kr.concert.domain.member;

import kr.concert.interfaces.member.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("존재하지 않는 회원은 포인트를 조회할 수 없다.")
    void ifMemberNotExistCanNotGetPoint() {
        // given
        Long memberId = 1L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty()) ;

        // when & then
        assertThatThrownBy(() -> memberService.getPoint(memberId))
                .isInstanceOf(MemberException.MemberNotFoundException.class)
                .hasMessage("Member Not Found");
    }

    @Test
    @DisplayName("존재하는 회원의 포인트를 조회한다.")
    void ifMemberExistCanGetPoint() {
        // given
        Long memberId = 1L;
        Long amount = 1000L;
        given(memberRepository.findById(memberId)).willReturn(
                Optional.of(new Member(1L, "김예찬", amount))
        ) ;

        // when
        Optional<Member> member = memberRepository.findById(memberId);
        Member result = member.get();
        
        // then
        assertThat(result.getMemberId()).isEqualTo(memberId);
        assertThat(result.getPoint()).isEqualTo(amount);
    }

    @Test
    @DisplayName("존재하지 않는 회원은 포인트를 충전할 수 없다.")
    void ifMemberNotExistCanNotChargePoint() {
        // given
        Long memberId = 1L;
        Long amount = 1000L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty()) ;

        // when & then
        assertThatThrownBy(() -> memberService.chargePoint(memberId, amount))
                .isInstanceOf(MemberException.MemberNotFoundException.class)
                .hasMessage("Member Not Found");
    }

    @Test
    @DisplayName("존재하는 회원은 포인트를 충전할 수 있다.")
    void ifMemberExistCanChargePoint() {
        // given
        given(memberRepository.findById(1L)).willReturn(
                Optional.of(new Member(1L, "김예찬", 1000L))
        ) ;

        // when
        Optional<Member> member = memberRepository.findById(1L);
        Member result = member.get();
        result.chargePoint(1000L);

        // then
        assertThat(result.getPoint()).isEqualTo(2000L);
    }

}