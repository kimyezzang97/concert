package kr.concert.interfaces.presentation;

import kr.concert.interfaces.member.MemberException;
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

}
