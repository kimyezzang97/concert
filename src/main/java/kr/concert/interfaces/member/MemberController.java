package kr.concert.interfaces.member;

import kr.concert.domain.member.MemberService;
import kr.concert.interfaces.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Member point 잔액 조회
     */
    @GetMapping("/{memberId}/points")
    public ApiResponse<MemberResponse.GetPoint> getPoint(@PathVariable Long memberId) {
        return new ApiResponse<>(true, 200, "ok", memberService.getPoint(memberId));
    }

    /**
     * Member point 충전
     */
    @PostMapping("/{memberId}/points/charge")
    public ApiResponse<MemberResponse.ChargePoint> chargePoint(@PathVariable Long memberId, @RequestBody MemberRequest.ChargePoint chargePointRequest ) {
        return new ApiResponse<>(true, 200, "ok", memberService.chargePoint(memberId, chargePointRequest.point()));
    }
}
