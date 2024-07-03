package tig.server.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tig.server.club.domain.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
}
