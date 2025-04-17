package kr.concert.domain.member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long memberId);
}
