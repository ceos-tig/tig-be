package tig.server.coupon.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tig.server.coupon.service.CouponService;

@Component
@RequiredArgsConstructor
public class CouponExpirationScheduler {
    private final CouponService couponService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void scheduleExpiredCouponDeletion() {
        couponService.markExpiredCouponsAsDeleted();
    }
}
