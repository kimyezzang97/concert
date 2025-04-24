package kr.concert.domain.reservation;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    EMPTY,
    TEMP,
    RESERVED,
    CANCELLED;
}
