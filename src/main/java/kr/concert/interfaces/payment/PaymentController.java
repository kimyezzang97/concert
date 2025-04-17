package kr.concert.interfaces.payment;

import kr.concert.application.payment.PaymentFacade;
import kr.concert.domain.payment.PaymentService;
import kr.concert.infra.config.swagger.api.SwaggerPaymentApi;
import kr.concert.interfaces.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController implements SwaggerPaymentApi {

    private final PaymentFacade paymentFacade;

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @PostMapping
    public ApiResponse<PaymentResponse.payment> createPayment(@RequestBody PaymentRequest.payment request) {

        return new ApiResponse<>(true, 200, "결제를 성공하였습니다.", paymentFacade.createPayment(request.reservationId(), request.memberId(), request.price())) ;
    }
}
