package tig.server.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tig.server.member.domain.Member;

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
