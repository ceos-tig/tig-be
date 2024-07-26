package tig.server.amenity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.amenity.domain.Amenity;

import java.util.List;
import java.util.Optional;

public interface AmenityRepository extends JpaRepository<Amenity,Long> {
    Optional<List<Amenity>> findByClubId(Long clubId);
}
