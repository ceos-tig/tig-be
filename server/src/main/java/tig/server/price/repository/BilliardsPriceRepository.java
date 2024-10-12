package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.BilliardsPrice;

import java.util.List;

public interface BilliardsPriceRepository extends JpaRepository<BilliardsPrice,Long> {
    // 특정 클럽의 당구 가격 조회
    List<BilliardsPrice> findByClub(Club club);
}
