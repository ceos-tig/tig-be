package tig.server.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Club c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") Long id);

    List<Club> findTop5ByOrderByRatingCountDesc();
}
