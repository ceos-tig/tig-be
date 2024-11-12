package tig.server.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import tig.server.coupon.domain.Coupon;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponResponseDto {

    @Schema(type = "long", example = "1")
    private Long couponId;

    @Schema(type = "integer", example = "5000")
    private Integer discount;

    @Schema(type = "string", example = "7월 Welcome 쿠폰")
    private String name;

    @Schema(type = "string", example = "금액 상관없이 20% 할인")
    private String description;

    @Schema(type = "string", example = "2024-12-03T10:15:30")
    private String expireDate;

    // 정적 팩토리 메서드
    public static CouponResponseDto from(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getDiscount(),
                coupon.getName(),
                coupon.getDescription(),
                coupon.getExpireDate() != null ? coupon.getExpireDate().toString() : null
        );
    }
}
