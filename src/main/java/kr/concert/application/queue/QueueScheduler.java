package kr.concert.application.queue;

import kr.concert.domain.queue.service.QueueService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QueueScheduler {

    private final QueueService queueService;

    public QueueScheduler(QueueService queueService) {
        this.queueService = queueService;
    }

    @Scheduled(fixedRate = 2000) // 2초마다 실행
    public void activateQueueStatus() {
        // 현재 대기열 PLAY 가 4명 이하이면, 대기열 상태 WAIT 을 PLAY 로 업데이트 하기 (5 - PLAY 수)
        queueService.activateQueueStatus();
    }

    @Scheduled(fixedRate = 2000) // 2초마다 실행
    public void runExpirePlayQueueJob() {
        // 만료기간이 지난 PLAY > EXPIRE 로 바꿔주기
        queueService.expireQueueStatus();
    }

}
