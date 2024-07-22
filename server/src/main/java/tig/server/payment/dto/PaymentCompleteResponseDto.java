package tig.server.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCompleteResponseDto {
    @Schema(type = "string", example = "PAID")
    private String status;

    @Schema(type = "string", example = "ed432870991f0ff268dd29ac1082d689")
    private String id;

    @Schema(type = "string", example = "KAKAOPAY")
    private String provider;

    @Schema(type = "string", example = "상품 명")
    private String orderName;

    @Schema(type = "integer", example = "10000")
    private Integer payedPrice;

    public static PaymentCompleteResponseDto fromPay(String status,
                                                     String id,
                                                     String provider,
                                                     String orderName,
                                                     Integer payedPrice) {
        return new PaymentCompleteResponseDto(status,id,provider,orderName,payedPrice);
    }
}
