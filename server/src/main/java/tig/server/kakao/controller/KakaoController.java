package tig.server.kakao.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tig.server.kakao.dto.KakaoUserInfoResponseDto;
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

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);

        LoginMemberResponseDto member = memberService.createMember(userInfo);

        // Access Token 쿠키 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", member.getAccessToken())
                .httpOnly(true)
                .path("/")
                .secure(true) // HTTPS를 사용할 경우에만 true로 설정
                .maxAge(24 * 60 * 60) // 24시간
                .sameSite("None")
                .build();

        // Refresh Token 쿠키 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .secure(true) // HTTPS를 사용할 경우에만 true로 설정
                .maxAge(14 * 24 * 60 * 60) // 2주
                .sameSite("None")
                .build();

        // 쿠키를 응답 헤더에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());


        log.info("Set-Cookie headers: {}", response.getHeaders(HttpHeaders.SET_COOKIE));
        log.info("Response headers: {}", response.getHeaderNames());

        return ResponseEntity.status(200).body(member.getAccessToken());
    }
}
