package kr.concert.domain.member;

import kr.concert.interfaces.member.MemberException;
import kr.concert.interfaces.member.MemberResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원의 포인트 잔액을 조회한다.
    public MemberResponse.GetPoint getPoint(Long memberId) {
        Optional<Member> member = memberRepository.getMember(memberId);
        if(member.isEmpty()) throw new MemberException.MemberNotFoundException();

        Member result = member.get();
        return new MemberResponse.GetPoint(result.getMemberId(), result.getMemberName(), result.getPoint());
    }

    // 회원의 포인트를 충전한다.
    public MemberResponse.ChargePoint chargePoint(Long memberId, Long chargePoint) {
        Optional<Member> member = memberRepository.getMember(memberId);
        if(member.isEmpty()) throw new MemberException.MemberNotFoundException();

        Member resultMember = member.get();
        resultMember.chargePoint(chargePoint, LocalDateTime.now());

        return new MemberResponse.ChargePoint(resultMember.getMemberId(), resultMember.getMemberName(), chargePoint);
    }



}
