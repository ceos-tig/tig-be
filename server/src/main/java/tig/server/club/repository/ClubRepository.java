package tig.server.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Club c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(@Param("id") Long id);

    List<Club> findTop5ByOrderByRatingCountDesc();

    @Query("SELECT c FROM Club c WHERE REPLACE(c.clubName, ' ', '') LIKE %:keyword% OR c.clubName LIKE %:keyword%")
    List<Club> searchByClubName(@Param("keyword") String keyword);

    @Query("SELECT c FROM Club c WHERE REPLACE(c.address, ' ', '') LIKE %:keyword% OR c.address LIKE %:keyword%")
    List<Club> searchByAddress(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM club c WHERE REPLACE(c.club_name, ' ', '') LIKE %:keyword% OR REPLACE(c.address, ' ', '') LIKE %:keyword%", nativeQuery = true)
    List<Club> searchByKeyword(@Param("keyword") String keyword);
}
