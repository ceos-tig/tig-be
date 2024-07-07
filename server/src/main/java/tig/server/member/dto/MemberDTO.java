package tig.server.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.MemberRoleEnum;

public class MemberDTO {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @Schema(type = "string", example = "kimin")
        private String name;

        @Schema(type = "string", example = "kimin@naver.com")
        private String email;

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber;

//        private String uniqueId;

        @Schema(type = "string", example = "src/image1.jpg")
        private String profileImage;

        @Schema(type = "string", example = "refreshToken1")
        private String refreshToken;

        @Schema(type = "string", example = "ROLE_USER")
        private MemberRoleEnum memberRoleEnum;

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        @Schema(type = "string", example = "kimin")
        private String name;

        @Schema(type = "string", example = "kimin@naver.com")
        private String email;

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber;

//        private String uniqueId;

        @Schema(type = "string", example = "src/image1.jpg")
        private String profileImage;

        @Schema(type = "string", example = "refreshToken1")
        private String refreshToken;

        @Schema(type = "string", example = "ROLE_USER")
        private MemberRoleEnum memberRoleEnum;

    }
}
