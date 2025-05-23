package kr.concert.interfaces.presentation;

import kr.concert.interfaces.member.MemberException;
import kr.concert.interfaces.queue.QueueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import kr.concert.interfaces.reservation.ReservationException;

@RestControllerAdvice // 전역설정을 위한 어노테이션
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.MemberNotFoundException.class)
    public ApiResponse<?> memberNotFound(MemberException.MemberNotFoundException e) {
         return new ApiResponse<>(false, 204, e.getMessage(), null);
    }

    @ExceptionHandler(MemberException.CanNotTooMuchChargeException.class)
    public ApiResponse<?> CanNotTooMuchChargeException(MemberException.CanNotTooMuchChargeException e) {
        return new ApiResponse<>(false, 400, e.getMessage(), null);
    }

    @ExceptionHandler(MemberException.CanNotMinusChargeException.class)
    public ApiResponse<?> CanNotMinusChargeException(MemberException.CanNotMinusChargeException e) {
        return new ApiResponse<>(false, 400, e.getMessage(), null);
    }

    @ExceptionHandler(ReservationException.ConcertNotExistException.class)
    public ApiResponse<?> ConcertNotExistException(ReservationException.ConcertNotExistException e) {
        return new ApiResponse<>(false, 204, e.getMessage(), null);
    }

    @ExceptionHandler(ReservationException.ScheduleNotExistException.class)
    public ApiResponse<?> ScheduleNotExistException(ReservationException.ScheduleNotExistException e) {
        return new ApiResponse<>(false, 204, e.getMessage(), null);
    }

    @ExceptionHandler(ReservationException.SeatNotExistException.class)
    public ApiResponse<?> SeatNotExistException(ReservationException.SeatNotExistException e) {
        return new ApiResponse<>(false, 204, e.getMessage(), null);
    }

    @ExceptionHandler(QueueException.TokenNotExistException.class)
    public ApiResponse<?> TokenNotExistException(QueueException.TokenNotExistException e) {
        return new ApiResponse<>(false, 401, e.getMessage(), null);
    }

    @ExceptionHandler(QueueException.TokenNotPlayException.class)
    public ApiResponse<?> TokenNotPlayException(QueueException.TokenNotPlayException e) {
        return new ApiResponse<>(false, 401, e.getMessage(), null);
    }

    @ExceptionHandler(ReservationException.ReservationNotExistException.class)
    public ApiResponse<?> ReservationNotExistException(ReservationException.ReservationNotExistException e) {
        return new ApiResponse<>(false, 400, e.getMessage(), null);
    }

    @ExceptionHandler(MemberException.InvalidAmountException.class)
    public ApiResponse<?> InvalidAmountException(MemberException.InvalidAmountException e) {
        return new ApiResponse<>(false, 400, e.getMessage(), null);
    }

    @ExceptionHandler(MemberException.InsufficientPointException.class)
    public ApiResponse<?> InsufficientPointException(MemberException.InsufficientPointException e) {
        return new ApiResponse<>(false, 400, e.getMessage(), null);
    }
}
