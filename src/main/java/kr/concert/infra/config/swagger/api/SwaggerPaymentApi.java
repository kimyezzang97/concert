package kr.concert.infra.config.swagger.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.concert.interfaces.member.MemberResponse;
import kr.concert.interfaces.payment.PaymentRequest;
import kr.concert.interfaces.payment.PaymentResponse;
import kr.concert.interfaces.presentation.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Payment", description = "결제 관련 API")
public interface SwaggerPaymentApi {

    @Operation(summary = "결제 API", description = "결제를 진행합니다.")
    ApiResponse<PaymentResponse.payment> createPayment(@RequestBody PaymentRequest.payment request);
}
