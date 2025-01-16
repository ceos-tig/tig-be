package tig.server.oauth2.kakao.controller;

import jakarta.servlet.http.Cookie;
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
import tig.server.oauth2.kakao.dto.KakaoUserInfoResponseDto;
import tig.server.oauth2.kakao.dto.LoginAccessTokenResponseDto;
import tig.server.oauth2.kakao.dto.LoginMemberResponseDto;
import tig.server.oauth2.kakao.service.KakaoService;
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
            kakaoAccessToken = kakaoService.getAccessTokenFromKakaoDeployTest(code);
        } else if(origin.equals("https://tigleisure.com")){
            kakaoAccessToken = kakaoService.getAccessTokenFromKakaoDeploy(code);
        }
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(kakaoAccessToken);
        LoginMemberResponseDto member = memberService.createKakaoMember(userInfo);


        if (origin.equals("https://localhost:3000") || origin.equals("https://localhost:8080")) {
            // localhost에 대한 쿠키 설정 (두 개의 쿠키 생성)
            ResponseCookie localCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
                    .httpOnly(true)
                    .secure(true) // 로컬 환경에서는 Secure=false
                    .path("/")
                    .domain("localhost") // localhost 도메인
                    .maxAge(14 * 24 * 60 * 60) // 2주 (초 단위)
                    .sameSite("None") // SameSite 설정
                    .build();

            ResponseCookie tigDomainCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .domain(".tigleisure.com") // .tigleisure.com 도메인
                    .maxAge(14 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            // 응답 헤더에 두 개의 쿠키 추가
            response.addHeader(HttpHeaders.SET_COOKIE, localCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, tigDomainCookie.toString());
        } else if (origin.equals("https://tigleisure.com")) {
            // 배포 환경에 대한 쿠키 설정
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
                    .httpOnly(true)
                    .secure(true) // HTTPS 환경
                    .path("/")
                    .domain(".tigleisure.com") // .tigleisure.com 도메인
                    .maxAge(14 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            // 응답 헤더에 쿠키 추가
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        }

        LoginAccessTokenResponseDto loginAccessTokenResponseDto = LoginAccessTokenResponseDto.fromMember(member.getAccessToken());
        ApiResponse<LoginAccessTokenResponseDto> result = ApiResponse.of(200, "Login Success", loginAccessTokenResponseDto);

        return ResponseEntity.ok(result);
    }
}
