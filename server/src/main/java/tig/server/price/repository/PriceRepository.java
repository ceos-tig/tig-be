package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.price.domain.Price;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price,Long> {
    Optional<Price> findAllByClubAndHour(Long clubId, int hour);
}
