package tig.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.coupon.dto.CouponCodeResponseDto;
import tig.server.coupon.dto.CouponIdRequestDto;
import tig.server.coupon.dto.CouponResponseDto;
import tig.server.coupon.service.CouponService;
import tig.server.global.response.ApiResponse;
import tig.server.member.domain.Member;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "사용자가 보유한 쿠폰 조회")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CouponResponseDto>>> getCoupons(@LoginUser Member member) {
        List<CouponResponseDto> couponList = couponService.getAllCouponsByMemberId(member.getId());
        ApiResponse<List<CouponResponseDto>> response = ApiResponse.of(200, "successfully retrieved coupons", couponList);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "발급된 쿠폰 번호로 쿠폰 등록")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerCoupon(@LoginUser Member member,
                                                            @RequestBody CouponIdRequestDto couponIdRequestDto) {
        couponService.registerCoupon(member.getId(), couponIdRequestDto.getCouponId());
        ApiResponse<Void> response = ApiResponse.of(200, "successfully registered coupon", null);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "ADMIN : 쿠폰 코드 발급")
    @GetMapping("/issue")
    public ResponseEntity<ApiResponse<CouponCodeResponseDto>> issueCouponCode() {
        CouponCodeResponseDto couponCodeResponseDto = couponService.issueCouponCode();
        ApiResponse<CouponCodeResponseDto> response = ApiResponse.of(200, "successfully issued coupon", couponCodeResponseDto);
        return ResponseEntity.ok(response);
    }
}
