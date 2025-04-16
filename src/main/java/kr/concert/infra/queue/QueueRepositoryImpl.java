package kr.concert.infra.queue;

import kr.concert.domain.queue.Queue;
import kr.concert.domain.queue.QueueRepository;
import kr.concert.domain.queue.QueueStatus;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    public QueueRepositoryImpl(QueueJpaRepository queueJpaRepository) {
        this.queueJpaRepository = queueJpaRepository;
    }

    @Override
    public Queue save(Queue queue) {
        return queueJpaRepository.save(queue);
    }

    @Override
    public Optional<Queue> findByToken(String token) {
        return queueJpaRepository.findByToken(token);
    }

    @Override
    public Long countByQueueStatusAndQueueIdLessThan(QueueStatus status, Long queueId) {
        return queueJpaRepository.countByQueueStatusAndQueueIdLessThan(status, queueId);
    }
}
