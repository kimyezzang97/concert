package kr.concert.infra.config.swagger.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.concert.interfaces.member.MemberResponse;
import kr.concert.interfaces.presentation.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Queue", description = "대기열 관련 API")
public interface SwaggerQueueApi {

    @Operation(summary = "대기열 토큰 생성", description = "해당 회원의 대기열 토큰을 생성합니다.")
    ApiResponse<String> createToken(@RequestParam Long memberId);

}
