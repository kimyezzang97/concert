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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class MemberConcurrencyTest extends TestContainerConfig {

    @Autowired
    private MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    // 500 point가 충전 되어야 하지만 현재 500 미만으로 충전된다.
    @Test
    @DisplayName("동시에 여러 번 충전해도 포인트는 정확히 누적된다.")
    void concurrentPointChargeTest() throws InterruptedException {
        Member member = new Member(null, "김예찬", 0L);
        memberRepository.save(member);

        Long memberId = member.getMemberId();

        int threadCount = 5;
        long chargeAmount = 200L;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    memberService.chargePoint(memberId, chargeAmount);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Member updated = memberRepository.findById(memberId).orElseThrow();
        assertEquals(chargeAmount * threadCount, updated.getPoint());
    }

    @Test
    @DisplayName("동시에 여러 번 차감해도 포인트는 음수로 내려가지 않는다.")
    void concurrentPointUseTest() throws InterruptedException {
        Member member = new Member(null, "김예찬", 1000L);
        memberRepository.save(member);

        Long memberId = member.getMemberId();

        int threadCount = 10;
        long useAmount = 200L;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            results.add(executor.submit(() -> {
                try {
                    // 각 스레드마다 트랜잭션을 따로 관리하려면 여기서 Member를 다시 조회해야 함
                    Member m = memberRepository.findById(memberId).orElseThrow();
                    memberService.paymentPoint(m, useAmount);
                    memberRepository.save(m);
                    return true;
                } catch (Exception e) {
                    return false;
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await();

        long successCount = results.stream()
                .filter(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        Member updated = memberRepository.findById(memberId).orElseThrow();

        System.out.println("남은 포인트: " + updated.getPoint());
        System.out.println("성공한 차감 횟수: " + successCount);

        // 1000 포인트로 200씩 차감하므로 최대 5번까지만 성공해야 함
        assertEquals(5, successCount); // 10번 성공함 나옴
        assertEquals(0L, updated.getPoint()); // 600원이나 남음
    }
}
