package kr.concert.interfaces.member;

public class MemberResponse {

    public record GetPoint(Long memberId, String memberName, Long point) {}

    public record ChargePoint(Long memberId, String memberName, Long point) {}

}
