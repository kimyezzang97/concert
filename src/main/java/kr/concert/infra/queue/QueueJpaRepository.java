package kr.concert.infra.queue;

import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.QueueStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<Queue, Long> {

    Optional<Queue> findByToken(String token);

    Long countByQueueStatusAndQueueIdLessThan(QueueStatus status, Long queueId);

    Optional<Queue> findByTokenAndMember_MemberId(String token, Long memberId);

    Integer countByQueueStatus(QueueStatus status);


    List<Queue> findTopNByQueueStatusOrderByQueueIdAsc(@Param("status") QueueStatus status, Pageable pageable);

    List<Queue> findAllByQueueStatusAndExpiredAtBefore(QueueStatus status, LocalDateTime now);
}
