package kr.concert.domain.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long scheduleId;
    private Long concertId;
    private LocalDateTime scheduleDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
