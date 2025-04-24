package kr.concert.application.queue;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.service.MemberService;
import kr.concert.domain.queue.service.QueueService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QueueFacade {

    private final MemberService memberService;
    private final QueueService queueService;

    public QueueFacade(MemberService memberService, QueueService queueService) {
        this.memberService = memberService;
        this.queueService = queueService;
    }

    // 대기열 토큰 생성
    @Transactional(rollbackFor = Exception.class)
    public String createToken(Long memberId){
        Member member = memberService.getMember(memberId);

        return queueService.createToken(member);
    }
}
