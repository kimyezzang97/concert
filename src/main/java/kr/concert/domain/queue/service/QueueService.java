package kr.concert.domain.queue.service;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.queue.QueueStatus;
import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.repo.QueueRepository;
import kr.concert.interfaces.queue.QueueException;
import kr.concert.interfaces.queue.QueueResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    private final int MAX_PLAY_NUM = 5; // PLAY(예매 가능) 상태의 최대 인원
    private final int EXPIRE_MINUTES = 5;

    @Transactional(rollbackFor = Exception.class)
    public String createToken(Member member) {
        String uuid = UUID.randomUUID().toString(); // 고유 토큰 생성
        Queue queue = Queue.create(member, uuid);

        return queueRepository.save(queue).getToken();
    }

    @Transactional(readOnly = true)
    public QueueResponse.QueueStatus getQueue(String token) {
        Queue queue = queueRepository.findByToken(token)
                .orElseThrow(QueueException.TokenNotExistException::new);

        // 순번 : 먼저 들어온 WAIT 상태 사용자 수 + 1
        long position = queueRepository.countByQueueStatusAndQueueIdLessThan(QueueStatus.WAIT , queue.getQueueId()) + 1;

        return new QueueResponse.QueueStatus(queue.getQueueStatus().toString(), position, queue.getExpiredAt());
    }

    public void checkToken(String token, Long memberId) {
        Queue queue = queueRepository.findByTokenAndMember_MemberId(token, memberId)
                .orElseThrow(QueueException.TokenNotExistException::new);

        if (!queue.getQueueStatus().equals(QueueStatus.PLAY)) throw new QueueException.TokenNotPlayException();
    }

    // PLAY
    @Transactional(rollbackFor = Exception.class)
    public void activateQueueStatus(){
        log.debug("=== start activate queue status ===");
        int currentPlayCount = queueRepository.countByQueueStatus(QueueStatus.PLAY);
        int batchSize = MAX_PLAY_NUM - currentPlayCount;

        if (batchSize <= 0) return;

        PageRequest pageRequest = PageRequest.of(0, batchSize);
        List<Queue> waitList = queueRepository.findTopNByQueueStatusOrderByQueueIdAsc(QueueStatus.WAIT, pageRequest);

        for (Queue queue : waitList) {
            queue.changeStatusToPlay(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
        }
    }

    // EXPIRE
    @Transactional(rollbackFor = Exception.class)
    public void expireQueueStatus(){
        log.debug("=== start expire queue status ===");
        List<Queue> willExiperList = queueRepository.findAllByQueueStatusAndExpiredAtBefore(QueueStatus.WAIT, LocalDateTime.now());

        for (Queue queue : willExiperList) {
            queue.expire();
        }
        log.debug(" Expiring queues: " + willExiperList.size());
        log.debug("=== end expire queue status ===");
    }

}
