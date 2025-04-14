package kr.concert.infra.member;

import kr.concert.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

}
