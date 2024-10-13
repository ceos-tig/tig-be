package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.BallingPrice;

import java.util.List;

public interface BallingPriceRepository extends JpaRepository<BallingPrice,Long> {
    // 특정 클럽에 해당하는 모든 볼링 가격 정보 조회
    List<BallingPrice> findByClub(Club club);
}
