package kr.concert.domain.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Seat {
    private Long seatId;
    private Long scheduleId;
    private Long seatNumber;
    private Long seatPrice;
    private boolean seatStatus; // 좌석 상태 POSSIBLE / IMPOSSIBLE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
