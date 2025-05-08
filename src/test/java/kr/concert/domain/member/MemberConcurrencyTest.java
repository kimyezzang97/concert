package kr.concert.domain.member;

import kr.concert.TestContainerConfig;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
import kr.concert.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class MemberConcurrencyTest extends TestContainerConfig {

    @Autowired
    private MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("동시에 여러 번 충전을 시도해도 다 충전된다.")
    void concurrentPointChargeTest() throws InterruptedException {
        Member member = Member.create("김예찬", 0L);
        memberRepository.save(member);

        Long memberId = member.getMemberId();

        int threadCount = 5;
        long chargeAmount = 200L;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger callCount = new AtomicInteger(0); // ✅ 호출 횟수 카운터

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    memberService.chargePoint(memberId, chargeAmount);
                    callCount.incrementAndGet(); // ✅ 호출 시 카운터 증가
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Member updated = memberRepository.findById(memberId).orElseThrow();
        assertEquals(chargeAmount * threadCount, updated.getPoint()); // chargeAmount 가 되어야 함
        assertEquals(threadCount, callCount.get(), "충전 호출 횟수는 정확히 200 번이어야 한다.");
    }

}
