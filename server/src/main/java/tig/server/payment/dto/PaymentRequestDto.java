package tig.server.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import tig.server.enums.Status;

@Getter
public class PaymentRequestDto {
    @Schema(type = "integer", example = "2")
    private Integer adultCount;

    @Schema(type = "integer", example = "0")
    private Integer teenagerCount;

    @Schema(type = "integer", example = "0")
    private Integer kidsCount;

    @Schema(type = "string", example = "2007-12-03T10:15:30")
    private String date;

    @Schema(type = "string", example = "2007-12-03T10:15:30")
    private String startTime;

    @Schema(type = "string", example = "2007-12-03T10:15:30")
    private String endTime;

    @Schema(type = "integer", example = "3")
    private Integer gameCount;

    @Schema(type = "integer", example = "30000")
    private Integer clubPrice;

    @Schema(type = "integer", example = "3")
    private Integer clubId;

    @Schema(type = "string", example = "2313dasd2asasd7d8as7d")
    private String paymentId;

    @Schema(type = "integer", example = "20000")
    private Integer paymentPrice;
}
