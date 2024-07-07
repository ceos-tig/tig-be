package tig.server.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tig.server.enums.Status;
import tig.server.reservation.domain.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberId(Long memberId);

    @Query("SELECT r FROM Reservation r WHERE r.member.id = :memberId AND r.status IN :statuses")
    List<Reservation> findReservationsByMemberId(@Param("memberId") Long memberId, @Param("statuses") List<Status> statuses);


}
