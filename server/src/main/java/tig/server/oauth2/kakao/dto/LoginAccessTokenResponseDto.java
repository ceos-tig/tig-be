package tig.server.oauth2.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginAccessTokenResponseDto {
    private String accessToken;

    public static LoginAccessTokenResponseDto fromMember(String accessToken) {
        return new LoginAccessTokenResponseDto(
                accessToken
        );
    }
}
