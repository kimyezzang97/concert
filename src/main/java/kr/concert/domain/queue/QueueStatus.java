package kr.concert.domain.queue;

public enum QueueStatus {
    WAIT,     // 대기 중
    PLAY,     // 서비스 이용 가능 (예약 등 가능)
    EXPIRE,   // 만료됨
    CANCEL    // 사용자가 대기열 취소
}
