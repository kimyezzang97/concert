package kr.concert.interfaces.reservation;

import java.time.LocalDateTime;

public class ReservationResponse {

    public record GetConcerts(Long concertId, String concertName, LocalDateTime createdAt) {}

    public record GetScheduleOfConcert(Long scheduleId, LocalDateTime scheduleDate, LocalDateTime createdAt) {}

    public record GetSeatsOfSchedule(Long seatId, Long scheduleId, Long seatNumber, Long seatPrice, Boolean seatStatus, LocalDateTime createdAt) {}

}
