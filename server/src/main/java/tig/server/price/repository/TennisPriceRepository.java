package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.TennisPrice;

import java.util.List;

public interface TennisPriceRepository extends JpaRepository<TennisPrice,Long> {
    // 특정 클럽의 테니스 가격 조회
    List<TennisPrice> findByClub(Club club);
}
