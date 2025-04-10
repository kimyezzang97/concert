package kr.concert.interfaces.reservation;

public class ReservationException {

    public static class ConcertNotExistException extends RuntimeException {
        public ConcertNotExistException() {
            super("Concert Not Exists");
        }
    }
}
