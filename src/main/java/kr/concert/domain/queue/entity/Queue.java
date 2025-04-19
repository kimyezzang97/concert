package kr.concert.domain.queue.entity;

import jakarta.persistence.*;
import kr.concert.domain.BaseEntity;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.queue.QueueStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name="queue")
public class Queue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id" ,nullable = false)
    private Long queueId;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 관리, DB에서 진짜 필요한 것만 쿼리로 날림.
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK 제약 X
    private Member member;

    @Column(name = "token", nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private QueueStatus queueStatus; // WAIT, PLAY, EXPIRE, CANCEL

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    // 대기열 토큰 생성 (정적 팩토리 메서드)
    public static Queue create(Member member, String token) {
        if(member == null) throw new IllegalArgumentException("회원 정보는 필수입니다.");
        if(token == null || token.isEmpty()) throw new IllegalArgumentException("토큰은 null 또는 빈 문자열일 수 없습니다.");

        Queue queue = new Queue();
        queue.member = member;
        queue.token = token;
        queue.queueStatus = QueueStatus.WAIT;
        return queue;
    }

    public void changeStatusToPlay(){
        queueStatus = QueueStatus.PLAY;
    }
}
