package kr.concert.application.payment;

import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRankRepository;
import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.service.MemberService;
import kr.concert.domain.payment.service.PaymentService;
import kr.concert.domain.reservation.ReservationStatus;
import kr.concert.domain.reservation.entity.Reservation;
import kr.concert.domain.reservation.service.ReservationService;
import kr.concert.infra.config.redis.DistributedLock;
import kr.concert.interfaces.payment.PaymentResponse;
import kr.concert.interfaces.reservation.ReservationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentFacade {

    private final PaymentService paymentService;
    private final MemberService memberService;
    private final ReservationService reservationService;
    private final ConcertRankRepository concertRankRepository;

    public PaymentFacade(PaymentService paymentService, MemberService memberService, ReservationService reservationService,
                         ConcertRankRepository concertRankRepository) {
        this.paymentService = paymentService;
        this.memberService = memberService;
        this.reservationService = reservationService;
        this.concertRankRepository = concertRankRepository;
    }

    @DistributedLock(key = "#memberId")
    public PaymentResponse.payment createPayment(long reservationId, long memberId, long price) {
        Member member = memberService.getMember(memberId);
        Reservation reservation = reservationService.getReservation(reservationId);

        // 회원 포인트 차감
        memberService.paymentPoint(member, price);

        // 예약 상태 변경
        reservation.confirmReservation();
        if(reservation.getReservationStatus() == ReservationStatus.CANCELLED) throw new ReservationException.ReservationNotExistException();

        // redis 좌석 개수 감소 + sold-out 시 랭킹 등록
        Concert concert = reservation.getSeat().getSchedule().getConcert();
        concertRankRepository.decreaseSeatCount(concert.getConcertId(), concert.getConcertName());

        // 결제 생성
        return paymentService.createPayment(reservation, member, price);
    }
}
