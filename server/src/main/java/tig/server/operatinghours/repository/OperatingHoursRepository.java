package tig.server.operatinghours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.operatinghours.domain.OperatingHours;

import java.util.List;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {
    List<OperatingHours> findByClub_Id(Long id);
}
