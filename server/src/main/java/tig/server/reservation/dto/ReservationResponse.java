package tig.server.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.Status;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    @Schema(type = "integer", example = "2")
    private Integer adultCount;

    @Schema(type = "integer", example = "0")
    private Integer teenagerCount;

    @Schema(type = "integer", example = "0")
    private Integer kidsCount;

    @Schema(type = "string", example = "2024-07-04")
    private String date;

    @Schema(type = "string", example = "10:00")
    private String startTime;

    @Schema(type = "string", example = "11:00")
    private String endTime;

    @Schema(type = "integer", example = "30000")
    private Integer price;

    @Schema(type = "string", allowableValues = {"TBC", "CONFIRMED", "CANCELED", "DECLINED", "DONE", "REVIEWED"}, example = "CONFIRMED")
    private Status status;

    @Schema(type = "integer", example = "2")
    private Integer memberId;

    @Schema(type = "integer", example = "3")
    private Integer clubId;
}
