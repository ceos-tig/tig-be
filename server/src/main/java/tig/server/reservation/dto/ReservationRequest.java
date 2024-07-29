package tig.server.reservation.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
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
    private Integer price;

    @Schema(type = "string", allowableValues = {"TBC", "CONFIRMED", "CANCELED", "DECLINED", "DONE", "REVIEWED"}, example = "CONFIRMED")
    private Status status;

    @Schema(type = "integer", example = "3")
    private Integer clubId;

    @Schema(type = "string", example = "2313dasd2asasd7d8as7d")
    private String paymentId;

    @Schema(type = "string", example = "잘 부탁 드립니다.")
    private String message;

    @Schema(type = "string", example = "예약할때 입력하는 이름")
    private String userName;

    @Schema(type = "string", example = "예약할때 입력하는 전화번호")
    private String phoneNumber;
}
