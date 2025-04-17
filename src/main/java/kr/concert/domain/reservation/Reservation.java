package kr.concert.domain.reservation;

import jakarta.persistence.*;
import kr.concert.domain.BaseEntity;
import kr.concert.domain.member.Member;
import kr.concert.domain.seat.Seat;
import kr.concert.interfaces.reservation.ReservationResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="reservation")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id" ,nullable = false)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 관리, DB에서 진짜 필요한 것만 쿼리로 날림.
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK 제약 X
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 관리, DB에서 진짜 필요한 것만 쿼리로 날림.
    @JoinColumn(name = "seat_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // FK 제약 X
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reservationStatus; // 예약 상태 : EMPTY / TEMP / RESERVED

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    public static Reservation create(Member member, Seat seat, LocalDateTime expiredAt) {
        if(member == null) throw new IllegalArgumentException("회원 정보는 필수입니다.");
        if(seat == null) throw new IllegalArgumentException("좌석 정보는 필수입니다.");
        if(expiredAt.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("만료 시간이 현재 시간보다 이전일 수 없습니다.");

        Reservation reservation = new Reservation();
        reservation.member = member;
        reservation.seat = seat;
        reservation.reservationStatus = ReservationStatus.TEMP;
        reservation.expiredAt = expiredAt;
        return reservation;
    }
}
