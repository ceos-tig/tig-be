package tig.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tig.server.member.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUniqueId(String uniqueId);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.isDeleted = true WHERE m.id = :id")
    void softDeleteById(@Param("id") Long id);

    Optional<Member> findByName(String memberName);
}
