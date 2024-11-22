package tig.server.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tig.server.coupon.domain.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    List<Coupon> findAllByMemberId(Long memberId);

    @Query("SELECT c FROM Coupon c WHERE c.expireDate < CURRENT_TIMESTAMP AND c.isDeleted = false")
    List<Coupon> findExpiredCoupons();

    @Query(value = "SELECT * FROM coupon WHERE coupon_id = :id", nativeQuery = true)
    Optional<Coupon> findByIdIgnoringSoftDelete(@Param("id") Long id);
}
