package kr.concert.domain.queue;

import kr.concert.domain.member.Member;
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
}
