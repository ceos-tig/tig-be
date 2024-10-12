package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.TableTennisPrice;

import java.util.List;

public interface TableTennisPriceRepository extends JpaRepository<TableTennisPrice, Long> {
    List<TableTennisPrice> findByClub(Club club);
}
