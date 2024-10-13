package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.BaseballPrice;

import java.util.List;

public interface BaseballPriceRepository extends JpaRepository<BaseballPrice, Long> {
    // 특정 클럽의 야구 가격 조회
    List<BaseballPrice> findByClub(Club club);
}
