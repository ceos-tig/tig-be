package tig.server.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.enums.PackageCategory;
import tig.server.enums.Status;
import tig.server.member.domain.Member;
import tig.server.reservation.domain.PackageReservation;

import java.util.List;

public interface PackageReservationRepository extends JpaRepository<PackageReservation, Long> {
}
