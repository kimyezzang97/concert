package kr.concert.infra.member;

import kr.concert.domain.member.Member;
import kr.concert.domain.member.MemberRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public MemberRepositoryImpl(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }
}
