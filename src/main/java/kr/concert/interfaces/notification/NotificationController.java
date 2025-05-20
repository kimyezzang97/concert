package kr.concert.interfaces.notification;

import kr.concert.domain.reservation.event.ReservationEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @PostMapping("") // Mock API
    public void notification(@RequestBody ReservationEvent reservationEvent) {
        System.out.println("Notify!!!");
        System.out.println(reservationEvent.getMemberName() + "님 " +
                reservationEvent.getSeatNumber() + " 좌석으로 예약되었습니다. 결제 부탁드립니다." );
    }

}
