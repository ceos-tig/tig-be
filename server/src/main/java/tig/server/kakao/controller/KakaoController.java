package tig.server.kakao.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tig.server.global.response.ApiResponse;
import tig.server.kakao.dto.KakaoUserInfoResponseDto;
import tig.server.kakao.dto.LoginAccessTokenResponseDto;
import tig.server.kakao.dto.LoginMemberResponseDto;
import tig.server.kakao.service.KakaoService;
import tig.server.member.service.MemberService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Slf4j
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    @RequestMapping("/callback")
    public ResponseEntity<ApiResponse<LoginAccessTokenResponseDto>> callback(HttpServletRequest request,
                                                                             @RequestParam("code") String code,
                                                                             HttpServletResponse response) throws IOException {
        String origin = request.getHeader("Origin");

        String kakaoAccessToken = null;
        if(origin.equals("https://localhost:3000") || origin.equals("https://localhost:8080")){
            kakaoAccessToken = kakaoService.getAccessTokenFromKakaoTest(code);
        } else if(origin.equals("https://main--testtig.netlify.app")) {
            kakaoAccessToken = kakaoService.getAccessTokenFromKakaoDeploy(code);
        }
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);
        LoginMemberResponseDto member = memberService.createMember(userInfo);

        // Refresh Token 쿠키 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .secure(true) // HTTPS를 사용할 경우에만 true로 설정
                .maxAge(14 * 24 * 60 * 60) // 2주
                .sameSite("None")
                .build();

        // 쿠키를 응답 헤더에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        LoginAccessTokenResponseDto loginAccessTokenResponseDto = LoginAccessTokenResponseDto.fromMember(member.getAccessToken());
        ApiResponse<LoginAccessTokenResponseDto> result = ApiResponse.of(200, "Login Success", loginAccessTokenResponseDto);

        return ResponseEntity.ok(result);
    }
}
