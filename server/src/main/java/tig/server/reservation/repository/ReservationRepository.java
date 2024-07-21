package tig.server.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tig.server.enums.Status;
import tig.server.reservation.domain.Reservation;
import tig.server.review.domain.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStatus(Status status);

    Optional<List<Reservation>> findByMemberId(Long memberId);

    @Query("SELECT r FROM Reservation r WHERE r.member.id = :memberId AND r.status IN :statuses")
    List<Reservation> findReservationsByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("statuses") List<Status> statuses);

    @Transactional
    @Modifying
    @Query("UPDATE Reservation r SET r.isDeleted = true WHERE r.id = :id")
    void softDeleteById(@Param("id") Long id);

    List<Reservation> findByReview(Review review);

}
