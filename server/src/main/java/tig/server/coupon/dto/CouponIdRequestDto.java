package tig.server.coupon.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.member.dto.RefreshTokenResponseDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIdRequestDto {

    private String couponId;

    public static CouponIdRequestDto fromCouponId(String couponId) {
        return new CouponIdRequestDto(couponId);
    }
}
