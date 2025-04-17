package kr.concert.domain.queue;


import java.util.Optional;

public interface QueueRepository {

    Queue save(Queue queue);

    Optional<Queue> findByToken(String token);

    Long countByQueueStatusAndQueueIdLessThan(QueueStatus status, Long queueId);

    Optional<Queue> findByTokenAndMember_MemberId(String token, Long memberId);
}
