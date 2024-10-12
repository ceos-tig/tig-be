package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.FootballPrice;

import java.util.List;

public interface FootballPriceRepository extends JpaRepository<FootballPrice,Long> {
    // 특정 클럽의 축구 가격 조회
    List<FootballPrice> findByClub(Club club);
}
