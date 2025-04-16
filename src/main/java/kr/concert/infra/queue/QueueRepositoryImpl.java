package kr.concert.infra.queue;

import kr.concert.domain.queue.Queue;
import kr.concert.domain.queue.QueueRepository;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.stereotype.Repository;

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
}
