package tig.server.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.coupon.domain.Coupon;
import tig.server.coupon.domain.CouponCode;
import tig.server.coupon.dto.CouponCodeResponseDto;
import tig.server.coupon.dto.CouponResponseDto;
import tig.server.coupon.repository.CouponCodeRepository;
import tig.server.coupon.repository.CouponRepository;
import tig.server.enums.MemberRoleEnum;
import tig.server.global.code.ErrorCode;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.member.domain.Member;
import tig.server.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
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
    public void registerCoupon(Long memberId, String couponId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        CouponCode code = couponCodeRepository.findByCode(couponId)
                .orElseThrow(() -> new BusinessExceptionHandler("coupon id not found", ErrorCode.NOT_FOUND_ERROR));

        code.markAsUsed();

        for (int i = 0; i < 5; i++) { // 10000원 * 5
            // 현재 한국 시간으로부터 30일 뒤의 23:59:59 계산
            ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
            LocalDateTime expireDate = ZonedDateTime.now(koreaZoneId)
                    .plusDays(30)
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59)
                    .withNano(0)
                    .toLocalDateTime();

            Coupon issuedCoupon = Coupon.builder()
                    .name("단체 회원 쿠폰")
                    .description("단체 회원(동아리 및 동호회)을 위한 10,000원 쿠폰")
                    .discount(10000)
                    .expireDate(expireDate) // 만료 날짜 설정
                    .member(member)
                    .build();

            couponRepository.save(issuedCoupon);
        }
    }

    @Transactional
    public CouponCodeResponseDto issueCouponCode() {
        // UUID 형식의 쿠폰 코드 생성
        String generatedCode = UUID.randomUUID().toString();
        CouponCode couponCode = CouponCode.builder()
                .code(generatedCode)
                .isUsed(false)
                .build();

        couponCodeRepository.save(couponCode);

        return new CouponCodeResponseDto(generatedCode);
    }
}
