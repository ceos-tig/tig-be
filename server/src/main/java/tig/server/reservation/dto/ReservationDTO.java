package tig.server.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.Status;

public class ReservationDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

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

        @Schema(type = "string", allowableValues = {"CONFIRMED", "TBC", "DECLINED"}, example = "CONFIRMED")
        private Status status;

        @Schema(type = "boolean", example = "false")
        private Boolean isReviewed = false;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

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

        @Schema(type = "string", allowableValues = {"CONFIRMED", "TBC", "DECLINED"}, example = "CONFIRMED")
        private Status status;

        @Schema(type = "boolean", example = "false")
        private Boolean isReviewed = false;

    }
}
