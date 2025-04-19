package kr.concert.infra.queue;

import kr.concert.domain.queue.entity.Queue;
import kr.concert.domain.queue.repo.QueueRepository;
import kr.concert.domain.queue.QueueStatus;
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

    @Override
    public Optional<Queue> findByTokenAndMember_MemberId(String token, Long memberId) {
        return queueJpaRepository.findByTokenAndMember_MemberId(token, memberId);
    }
}
