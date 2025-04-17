package kr.concert.interfaces.queue;

public class QueueException {

    public static class TokenNotExistException extends RuntimeException {
        public TokenNotExistException() {
            super("Token Not Exists");
        }
    }

    public static class TokenNotPlayException extends RuntimeException {
        public TokenNotPlayException() {
            super("Token Not Play");
        }
    }
}
