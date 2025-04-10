package kr.concert.infra.impl;

import kr.concert.domain.member.Member;
import kr.concert.domain.member.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @Override
    public Optional<Member> getMember(Long memberId) {
        return Optional.empty();
    }
}
