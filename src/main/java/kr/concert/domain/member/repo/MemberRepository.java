package kr.concert.domain.member.repo;

import kr.concert.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(Long memberId);
}
