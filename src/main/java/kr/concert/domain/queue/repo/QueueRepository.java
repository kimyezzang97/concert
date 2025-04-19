package kr.concert.domain.queue.repo;


import kr.concert.domain.queue.QueueStatus;
import kr.concert.domain.queue.entity.Queue;

import java.util.Optional;

public interface QueueRepository {

    Queue save(Queue queue);

    Optional<Queue> findByToken(String token);

    Long countByQueueStatusAndQueueIdLessThan(QueueStatus status, Long queueId);

    Optional<Queue> findByTokenAndMember_MemberId(String token, Long memberId);
}
