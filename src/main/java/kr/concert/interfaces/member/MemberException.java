package kr.concert.interfaces.member;

public class MemberException {

    public static class MemberNotFoundException extends RuntimeException {
        public MemberNotFoundException() {
            super("Member Not Found");
        }
    }

    public static class CanNotTooMuchChargeException extends RuntimeException {
        public CanNotTooMuchChargeException() {
            super("Are you rich?");
        }
    }

    public static class CanNotMinusChargeException extends RuntimeException {
        public CanNotMinusChargeException() {
            super("You can't charge 0 or less!");
        }
    }

    public static class InvalidAmountException extends RuntimeException {
        public InvalidAmountException() {
            super("You can't payment 0 or less!");
        }
    }

    public static class InsufficientPointException extends RuntimeException {
        public InsufficientPointException() {
            super("Not Enough Points");
        }
    }

}
