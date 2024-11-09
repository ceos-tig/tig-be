package tig.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tig.server.annotation.LoginUser;
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

    @GetMapping("")
    @Operation(summary = "사용자가 보유한 쿠폰 조회")
    public ResponseEntity<ApiResponse<List<CouponResponseDto>>> getCoupons(@LoginUser Member member) {
        List<CouponResponseDto> couponList = couponService.getAllCouponsByMemberId(member.getId());
        ApiResponse<List<CouponResponseDto>> response = ApiResponse.of(200, "successfully retrieved coupons", couponList);
        return ResponseEntity.ok(response);
    }
}
