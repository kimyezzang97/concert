package kr.concert.domain.seat;

import jakarta.persistence.*;
import kr.concert.domain.BaseEntity;
import kr.concert.domain.schedule.Schedule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="seat")
public class Seat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id" ,nullable = false)
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 관리, DB에서 진짜 필요한 것만 쿼리로 날림.
    @JoinColumn(name = "schedule_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK 제약 X
    private Schedule schedule;

    @Column(name = "seat_number", nullable = false)
    private Long seatNumber;

    @Column(name = "seat_price", nullable = false)
    private Long seatPrice;

    @Column(name = "seat_status", nullable = false)
    private boolean seatStatus; // 좌석 상태 POSSIBLE / IMPOSSIBLE

    public void reserveSeatStatus(){
        this.seatStatus = false;
    }
}
