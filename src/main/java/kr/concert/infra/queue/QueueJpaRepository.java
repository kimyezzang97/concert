package kr.concert.infra.queue;

import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<Queue, Long> {

    Optional<Queue> findByToken(String token);

    Long countByQueueStatusAndQueueIdLessThan(QueueStatus status, Long queueId);

    Optional<Queue> findByTokenAndMember_MemberId(String token, Long memberId);
}
