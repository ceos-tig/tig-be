package tig.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUniqueId(String uniqueId);
}
