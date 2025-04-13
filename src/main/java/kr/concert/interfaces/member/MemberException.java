package kr.concert.interfaces.member;

import lombok.Getter;

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
            super("Are you kidding me?");
        }
    }

}
