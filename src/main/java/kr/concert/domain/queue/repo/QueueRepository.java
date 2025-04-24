package kr.concert.domain.queue.repo;


import kr.concert.domain.queue.QueueStatus;
import kr.concert.domain.queue.entity.Queue;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueRepository {

    Queue save(Queue queue);

    Optional<Queue> findByToken(String token);

    Long countByQueueStatusAndQueueIdLessThan(QueueStatus status, Long queueId);

    Optional<Queue> findByTokenAndMember_MemberId(String token, Long memberId);

    Integer countByQueueStatus(QueueStatus status);

    List<Queue> findTopNByQueueStatusOrderByQueueIdAsc(QueueStatus status, Pageable pageable);

    List<Queue> findAllByQueueStatusAndExpiredAtBefore(QueueStatus status, LocalDateTime now);
}
