package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.GolfPrice;

import java.util.List;

public interface GolfPriceRepository extends JpaRepository<GolfPrice,Long> {
    // 특정 클럽의 골프 가격 조회
    List<GolfPrice> findByClub(Club club);
}
