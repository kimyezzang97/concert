package kr.concert.interfaces.queue;


import java.time.LocalDateTime;

public class QueueResponse {

    public record QueueStatus(String status, Long position, LocalDateTime expiredAt) {}
}
