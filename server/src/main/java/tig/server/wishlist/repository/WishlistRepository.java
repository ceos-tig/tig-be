package tig.server.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tig.server.enums.Status;
import tig.server.reservation.domain.Reservation;
import tig.server.wishlist.domain.Wishlist;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Wishlist w SET w.isDeleted = true WHERE w.id = :id")
    void softDeleteById(@Param("id") Long id);

    List<Wishlist> findAllByMemberId(Long memberId);

    Optional<Wishlist> findByMemberIdAndClubId(Long memberId, Long ClubId);

}
