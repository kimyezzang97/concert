package kr.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Concert {
    private Long concertId;
    private String concertName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
