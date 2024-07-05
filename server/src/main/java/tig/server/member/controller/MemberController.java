package tig.server.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tig.server.error.ApiResponse;
import tig.server.jwt.TokenProvider;
import tig.server.member.domain.Member;
import tig.server.member.dto.RefreshTokenRequestDto;
import tig.server.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    /**
     * refresh token을 통한 access token 재발급
     */
    @PostMapping("/reissue")
    public ApiResponse<Object> reissueAccessToken(/*@LoginUser*/ Member member,
                                                  @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String newAccessToken = memberService.reissueAccessToken(member, refreshTokenRequestDto);
        return ApiResponse.builder()
                .result(newAccessToken)
                .resultMsg("Access token 재발급")
                .resultCode(200)
                .build();
    }
}
