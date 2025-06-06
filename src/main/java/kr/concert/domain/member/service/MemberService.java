package kr.concert.domain.member.service;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
import kr.concert.infra.config.redis.DistributedLock;
import kr.concert.interfaces.member.MemberException;
import kr.concert.interfaces.member.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    @DistributedLock(key = "#memberId")
    public MemberResponse.ChargePoint chargePoint(Long memberId, Long chargePoint) {
        Member resultMember = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MemberNotFoundException::new);

        resultMember.chargePoint(chargePoint);

        return new MemberResponse.ChargePoint(resultMember.getMemberId(), resultMember.getMemberName(), resultMember.getPoint());
    }

    // memberId로 회원을 조회한다.
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberException.MemberNotFoundException::new);
    }

    public void paymentPoint(Member member, Long amount){
        if (amount == null || amount <= 0) throw new MemberException.InvalidAmountException();
        if (member.getPoint() < amount) throw new MemberException.InsufficientPointException();

        member.paymentPoint(amount);
    }

}
