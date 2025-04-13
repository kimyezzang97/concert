package kr.concert.interfaces.reservation;

public class ReservationException {

    public static class ConcertNotExistException extends RuntimeException {
        public ConcertNotExistException() {
            super("Concert Not Exists");
        }
    }

    public static class ScheduleNotExistException extends RuntimeException {
        public ScheduleNotExistException() {
            super("Schedule Not Exists");
        }
    }

    public static class SeatNotExistException extends RuntimeException {
        public SeatNotExistException() {
            super("Seat Not Exists");
        }
    }
}
