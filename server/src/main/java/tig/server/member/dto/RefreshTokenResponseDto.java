package tig.server.member.dto;

import lombok.Getter;

@Getter
public class RefreshTokenResponseDto {
    private String refreshToken;
    private String accessToken;
}
