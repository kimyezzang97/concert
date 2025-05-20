package kr.concert.domain.reservation.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEvent {
    private String memberName;
    private Long seatNumber;
}
