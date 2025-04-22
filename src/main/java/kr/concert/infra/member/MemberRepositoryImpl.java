package kr.concert.infra.member;

import kr.concert.domain.member.entity.Member;
import kr.concert.domain.member.repo.MemberRepository;
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

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }
}
