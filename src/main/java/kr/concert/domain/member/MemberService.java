package kr.concert.domain.member;

import jakarta.persistence.Table;
import kr.concert.interfaces.member.MemberException;
import kr.concert.interfaces.member.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원의 포인트 잔액을 조회한다.
    @Transactional(readOnly = true)
    public MemberResponse.GetPoint getPoint(Long memberId) {
        Member resultMember = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MemberNotFoundException::new);

        return new MemberResponse.GetPoint(resultMember.getMemberId(), resultMember.getMemberName(), resultMember.getPoint());
    }

    // 회원의 포인트를 충전한다.
    @Transactional(rollbackFor = Exception.class)
    public MemberResponse.ChargePoint chargePoint(Long memberId, Long chargePoint) {
        Member resultMember = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MemberNotFoundException::new);

        resultMember.chargePoint(chargePoint);

        return new MemberResponse.ChargePoint(resultMember.getMemberId(), resultMember.getMemberName(), resultMember.getPoint());
    }



}
