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
import java.util.Set;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Wishlist w SET w.isDeleted = true WHERE w.id = :id")
    void softDeleteById(@Param("id") Long id);

    List<Wishlist> findAllByMemberId(Long memberId);

    @Query("SELECT w.club.id FROM Wishlist w WHERE w.member.id = :memberId")
    Set<Long> findLikedClubIds(@Param("memberId") Long memberId);

    Optional<Wishlist> findByMemberIdAndClubId(Long memberId, Long ClubId);

    @Query(value = "SELECT * FROM wishlist WHERE club_id = :clubId AND member_id = :memberId", nativeQuery = true)
    Optional<Wishlist> findAllByClubIdAndMemberId(@Param("clubId") Long clubId, @Param("memberId") Long memberId);

    boolean existsByClubIdAndMemberId(Long clubId, Long memberId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wishlist SET is_deleted = false, updated_at = CURRENT_TIMESTAMP WHERE club_id = :clubId AND member_id = :memberId AND is_deleted = true", nativeQuery = true)
    void restoreWishlist(@Param("clubId") Long clubId, @Param("memberId") Long memberId);
}
