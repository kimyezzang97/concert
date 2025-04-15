package kr.concert.interfaces.reservation;

import java.time.LocalDateTime;

public class ReservationResponse {

    public record GetConcerts(Long concertId, String concertName, LocalDateTime createdAt) {}

    public record GetScheduleOfConcert(Long scheduleId, String concertName, LocalDateTime scheduleDate, LocalDateTime createdAt) {}

    public record GetSeatsOfSchedule(String concertName, Long seatId, Long scheduleId, Long seatNumber, Long seatPrice, Boolean seatStatus, LocalDateTime createdAt) {}

}
