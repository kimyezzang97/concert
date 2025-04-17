package kr.concert.domain.queue;

import kr.concert.domain.member.Member;
import kr.concert.interfaces.member.MemberException;
import kr.concert.interfaces.queue.QueueException;
import kr.concert.interfaces.queue.QueueResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class QueueService {

    private final QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

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

        long position = queueRepository.countByQueueStatusAndQueueIdLessThan(QueueStatus.WAIT , queue.getQueueId()) + 1;

        return new QueueResponse.QueueStatus(queue.getQueueStatus().toString(), position, queue.getExpiredAt());
    }

    public void checkToken(String token, Long memberId) {
        Queue queue = queueRepository.findByTokenAndMember_MemberId(token, memberId)
                .orElseThrow(QueueException.TokenNotExistException::new);

        if (!queue.getQueueStatus().equals(QueueStatus.PLAY)) throw new QueueException.TokenNotPlayException();
    }
}
