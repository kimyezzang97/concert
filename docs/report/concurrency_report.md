# 🚦 동시성 이슈 보고서

---

### ❓ 동시성 문제란 무엇인가요?
여러 작업(프로세스, 스레드 등)이 공통 자원(메모리, DB)에 동시에 접근하여 수정하려고 때 발생하는 문제입니다.

### 🎯 락의 범위는 어떻게 설정하는 것이 좋을까요?
- 락의 범위는 가능한 작게 설정하는 것이 좋습니다. 
- 락의 범위를 넓게 잡는 경우 처리량이 떨어지는 문제가 발생할 수 있습니다.
- 반면, 락의 범위를 좁히면 처리량을 중가 시킬 수 있습니다. ⚡
- 그렇다고 락을 지나치게 작게 설정 하면 **동시성 문제**가 발생할 수 있으므로 주의가 필요합니다.   
- 즉! ✅ 동시성 문제가 발생하지 않는 선에서 가능한 범위를 최소화하여 설정하는 것이 가장 효율적입니다.

### 🧠 낙관적 락(Optimistic Lock)은 무엇일까요?
- 버전(version) 정보를 비교해서 변경 충돌 여부를 확인합니다.
- 🔁 동작 방식
  1. 데이터를 읽을 때 version 값도 같이 읽습니다.
  2. 데이터를 수정 후 저장할 때, version 값이 변경되지 않았는지 체크합니다.
  3. 누가 먼저 수정해서 version 이 달라졌다면 충돌을 감지합니다.
- 보통 동시에 한 자원에 접근한 후 한 번만 성공해도 될 때 사용합니다.
- ✅ 장점
  - 성능이 좋고 처리량이 좋습니다.
- ⚠️ 단점
  - 충돌이 빈번한 환경에서는 효율이 낮아질 수 있습니다.
- ✔️ 예시
  - 동일 좌석을 여러 사용자가 동시에 선점하려 할 경우, 결국 한 명만 성공하고 나머지는 실패합니다. 
  - 이런 상황에서는 대기보다는 빠른 실패 처리가 효율적이며, 이를 위해 낙관적 락을 적용하는 것이 더 합리적입니다.

### 🔒 비관적 락(Pessimistic Lock)은 무엇일까요?
- 특정 자원에 대해 Lock 설정으로 선점해 다른 트랜잭션이 접근 불가하게 막는 방법입니다.
- 🔁 동작 방식
  1. 데이터를 접근할 때 DB에 SELECT ... FOR UPDATE 실행
  2. 해당 데이터에 다른 트랜잭션은 접근 불가
  3. 트랜잭션 종료시 락 해제
- ✅ 장점
  - 데이터 충돌이 잦은 환경에서도 일관성과 무결성을 보장합니다.
- ⚠️ 단점
  - 트랜잭션 대기 시간 증가로 시스템 성능이 저하할 수 있습니다.
- ✔️ 예시
  - 재고 차감처럼 앞 요청이 끝난 후 내 것도 꼭! 처리 되어야 하는 경우 적합합니다.
  - 동시에 요청이 들어와도 모두 순차적으로 처리됩니다.


---

## 🛠️ 프로젝트의 동시성 문제

### 1. 회원의 포인트
- 🔥 문제
  - 회원이 동시에 포인트를 충전할 경우 예상된 양 만큼 충전이 되지 않습니다. 
- 💡 적용
  - 1명의 회원이 포인트를 동시에 충전할 가능성이 적어 충돌 가능성이 낮으므로 낙관적 락을 적용하였습니다.
- 📈 결과
  - ⛔ [적용 전] 스레드 수(2번) 만큼의 충전이 호출 되었지만 정작 충전된 포인트는 1번 호출된 양이었습니다.
  - ✅ [적용 후] 2번의 시도가 들어왔지만, 호출은 1번만 되었고 충전된 포인트도 1번 호출된 양이었습니다.
```java
// Member 도메인
@Entity(name="member")
public class Member extends BaseEntity {
    @Version
    private Long version;
}
```

```java
// 테스트 코드
@Test
@DisplayName("동시에 여러 번 충전해도 포인트는 정확히 누적된다.")
void concurrentPointChargeTest() throws InterruptedException {
  Member member = Member.create("김예찬", 0L);
  memberRepository.save(member);

  Long memberId = member.getMemberId();

  int threadCount = 2;
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
  assertEquals(chargeAmount, updated.getPoint()); // chargeAmount 가 되어야 함
  assertEquals(1, callCount.get(), "충전 호출 횟수는 정확히 1 번이어야 한다.");
}
```

### 2. 콘서트 좌석 예약
- 🔥 문제
  - 1명의 회원 혹은 여러명의 회원이 동시에 좌석을 예약할 경우 1개 초과로 좌석이 예약 되었습니다.
- 💡 적용
  - 대기열로 인원이 제한이 되어 동시에 예약을 할 가능성이 적어 충돌 가능성이 낮으므로 판단을 해서 낙관적 락을 적용하였습니다.
- 📈 결과
  - ⛔ [적용 전] 5개의 스레드가 동시에 좌석을 예약할 경우 3개의 예약이 생성되었습니다.
  - ✅ [적용 후] 5개의 스레드가 동시에 좌석을 예약하였지만 1개의 예약만 생성되었습니다.
```java
// seat 도메인
@Entity(name="seat")
public class Seat extends BaseEntity {
  @Version // ✅ 낙관적 락 적용
  private Long version;
}
```

```java
// 테스트 코드
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

  Seat seat = new Seat(null,null, schedule, 1L, 10L, true);
  seatRepository.save(seat);

  int THREAD_COUNT = 5;
  ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT); // 동시성 테스트를 위해 ExecutorService 로 스레드 풀 만듬
  CountDownLatch latch = new CountDownLatch(THREAD_COUNT); // CountDownLatch로 모든 스레드의 종료를 기다림.

  List<Future<Boolean>> results = new ArrayList<>();

  /**
   * 같은 좌석을 동시에 예약을 시도함.
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
```