package kr.concert.infra.queue;

import kr.concert.domain.queue.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueJpaRepository extends JpaRepository<Queue, Long> {
}
