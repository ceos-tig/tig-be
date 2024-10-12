package tig.server.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.price.domain.SquashPrice;

import java.util.List;

public interface SquashPriceRepository extends JpaRepository<SquashPrice,Long> {
    // 특정 클럽의 스쿼시 가격 조회
    List<SquashPrice> findByClub(Club club);
}
