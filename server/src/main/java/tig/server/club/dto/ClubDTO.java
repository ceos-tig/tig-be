package tig.server.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.Category;
import tig.server.enums.Type;

public class ClubDTO {
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "string", example = "티그볼링장")
        private String clubName;

        @Schema(type = "string", example = "서울시 서대문구 52-15")
        private String address;

        @Schema(type = "float", example = "4.5")
        private Float rating;

        @Schema(type = "integer", example = "30000")
        private Integer price;

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber;

        @Schema(type = "string", example = "https://instagram.com/tigballing")
        private String snsLink;

        @Schema(type = "string", example = "10:00 - 3:00")
        private String businessHours;

        @Schema(type = "float", example = "129.0921")
        private Float latitude;

        @Schema(type = "float", example = "52.0123")
        private Float longitude;

        @Schema(type = "string", example = "BALLING")
        private Category category;

        @Schema(type = "string", example = "GAME")
        private Type type;

    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @Schema(type = "string", example = "티그볼링장")
        private String clubName;

        @Schema(type = "string", example = "서울시 서대문구 52-15")
        private String address;

        @Schema(type = "float", example = "4.5")
        private Float rating;

        @Schema(type = "integer", example = "30000")
        private Integer price;

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber;

        @Schema(type = "string", example = "https://instagram.com/tigballing")
        private String snsLink;

        @Schema(type = "string", example = "10:00 - 3:00")
        private String businessHours;

        @Schema(type = "float", example = "129.0921")
        private Float latitude;

        @Schema(type = "float", example = "52.0123")
        private Float longitude;

        @Schema(type = "string", example = "BALLING")
        private Category category;

        @Schema(type = "string", example = "GAME")
        private Type type;

    }
}
