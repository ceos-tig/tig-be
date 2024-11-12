package tig.server.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.coupon.domain.Coupon;
import tig.server.coupon.domain.CouponCode;
import tig.server.coupon.dto.CouponResponseDto;
import tig.server.coupon.repository.CouponCodeRepository;
import tig.server.coupon.repository.CouponRepository;
import tig.server.enums.MemberRoleEnum;
import tig.server.global.code.ErrorCode;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.member.domain.Member;
import tig.server.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final CouponCodeRepository couponCodeRepository;

    public List<CouponResponseDto> getAllCouponsByMemberId(Long memberId) {
        List<Coupon> couponList = couponRepository.findAllByMemberId(memberId);

        // Coupon 객체를 CouponResponseDto로 변환하여 리스트에 추가
        return couponList.stream()
                .map(CouponResponseDto::from)  // 정적 팩토리 메서드를 사용하여 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public void registerCoupon(Long memberId , String couponId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        if(!member.getMemberRoleEnum().equals(MemberRoleEnum.ADMIN)) // 관리자가 아니라면 에러 발생
            throw new BusinessExceptionHandler("NOT ALLOWED", ErrorCode.FORBIDDEN_ERROR);
        else { // 관리자일 경우
            CouponCode couponCode = CouponCode.builder()
                    .code(couponId)
                    .isUsed(false)
                    .build();
            couponCodeRepository.save(couponCode);
        }
    }

    public void issueCoupon(String couponId) {
        CouponCode coupon = couponCodeRepository.findByCode(couponId)
                .orElseThrow(() -> new BusinessExceptionHandler("coupon id not found", ErrorCode.NOT_FOUND_ERROR));
    }
}
