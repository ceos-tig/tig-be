package tig.server.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.coupon.domain.CouponCode;

import java.util.Optional;

public interface CouponCodeRepository extends JpaRepository<CouponCode, Long> {
    Optional<CouponCode> findByCode(String code);
}
