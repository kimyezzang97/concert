package kr.concert.domain.reservation;

import kr.concert.TestContainerConfig;
import kr.concert.application.reservation.ReservationFacade;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.repo.QueueRepository;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.schedule.repo.ScheduleRepository;
import kr.concert.domain.seat.entity.Seat;
import kr.concert.domain.seat.repo.SeatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
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
public class ReservationConcurrencyTest extends TestContainerConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("delete from member");
        jdbcTemplate.execute("delete from seat");
        jdbcTemplate.execute("delete from concert");
        jdbcTemplate.execute("delete from schedule");
        jdbcTemplate.execute("delete from reservation");
        jdbcTemplate.execute("delete from queue");
    }

    @Test
    @DisplayName("동시에 좌석을 예약하면 단 하나만 성공한다.") // 현재는 동시성 에러가 발생한다.
    void ifReserveSameWillHaveOne() throws InterruptedException {
        Member member = Member.create( "김예찬", 1000L);
        memberRepository.save(member);

        Queue queue = Queue.create(member, "test");
        queue.changeStatusToPlay(LocalDateTime.now().plusDays(1));
        queueRepository.save(queue);

        Concert concert = new Concert(null, "카라 콘서트");
        concertRepository.save(concert);

        Schedule schedule = new Schedule(null, concert, LocalDateTime.now().plusDays(1));
        scheduleRepository.save(schedule);

        Seat seat = new Seat(null, schedule, 1L, 10L, true);
        seatRepository.save(seat);

        int THREAD_COUNT = 5;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT); // 동시성 테스트를 위해 ExecutorService 로 스레드 풀 만듬
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT); // CountDownLatch로 모든 스레드의 종료를 기다림.

        List<Future<Boolean>> results = new ArrayList<>();

        /**
         * 같은 좌석에 대해 여러 명이 동시에 예약을 시도함. 모두 같은 seatId, memberId, token 사용.
         */
        for (int i = 0; i < THREAD_COUNT; i++) {
            results.add(executor.submit(() -> {
                try {
                    reservationFacade.reserve(seat.getSeatId(), 1L, "test");
                    return true;
                } catch (Exception e) {
                    //e.printStackTrace();
                    return false;
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await(); // 모든 스레드 작업이 끝날 때까지 대기.

        long successCount = results.stream()
                .filter(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        // 한 개가 들어와야 한다.
        assertEquals(1, successCount);
    }
}
