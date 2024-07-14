package tig.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tig.server.kakao.dto.LoginMemberResponseDto;
import tig.server.member.domain.Member;

@Getter
@AllArgsConstructor
public class RefreshTokenResponseDto {
    private String refreshToken;
    private String accessToken;

    // Static factory method
    public static RefreshTokenResponseDto fromRefreshToken(String accessToken,String refreshToken) {
        return new RefreshTokenResponseDto(
                refreshToken,
                accessToken
        );
    }
}
