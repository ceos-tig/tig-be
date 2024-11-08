package tig.server.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.coupon.domain.Coupon;
import tig.server.coupon.dto.CouponResponseDto;
import tig.server.coupon.repository.CouponRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponseDto> getAllCouponsByMemberId(Long memberId) {
        List<Coupon> couponList = couponRepository.findAllByMemberId(memberId);

        // Coupon 객체를 CouponResponseDto로 변환하여 리스트에 추가
        return couponList.stream()
                .map(CouponResponseDto::from)  // 정적 팩토리 메서드를 사용하여 변환
                .collect(Collectors.toList());
    }
}
