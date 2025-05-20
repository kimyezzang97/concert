package kr.concert.interfaces.event.reservation;

import kr.concert.domain.reservation.event.ReservationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ReservationEventListener {

    private final RestTemplate restTemplate = new RestTemplate();

    @Async // 비동기 처리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 예약 커밋 후 수행
    public void NotifyPublish(ReservationEvent event) {
        // Mock API
        String url = "http://localhost:8080/notifications";

        // POST 요청
        try {
            restTemplate.postForEntity(url, event, Void.class);
        } catch (Exception e) {
            log.error("알림중 예외가 발생하였습니다 {}", e.getMessage());
        }

    }
}
