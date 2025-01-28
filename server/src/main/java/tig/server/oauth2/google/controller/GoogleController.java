package tig.server.oauth2.google.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tig.server.global.response.ApiResponse;
import tig.server.member.service.MemberService;
import tig.server.oauth2.google.dto.GoogleInfoResponseDto;
import tig.server.oauth2.google.service.GoogleService;
import tig.server.oauth2.kakao.dto.LoginAccessTokenResponseDto;
import tig.server.oauth2.kakao.dto.LoginMemberResponseDto;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GoogleController {
    private final GoogleService googleService;
    private final MemberService memberService;

    @RequestMapping("/google/callback")
    public ResponseEntity<ApiResponse<LoginAccessTokenResponseDto>> callback(HttpServletRequest request,
                                                                             @RequestParam("code") String code,
                                                                             HttpServletResponse response) throws IOException {
        String origin = request.getHeader("Origin");

        GoogleInfoResponseDto googleMemberInfo = null;
        if(origin.equals("https://localhost:3000") || origin.equals("https://localhost:8080")){
            googleMemberInfo = googleService.getGoogleMemberInfoTest(code);
        } else if(origin.equals("https://tigleisure.com")){
            googleMemberInfo = googleService.getGoogleMemberInfoDeploy(code);
        }
        LoginMemberResponseDto member = memberService.createGoogleMember(googleMemberInfo);


        if ("https://localhost:3000".equals(origin) || "https://localhost:8080".equals(origin)) { // 로컬에는 body와 쿠키 모두 전송
            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", member.getAccessToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .domain(".tigleisure.com") // .tigleisure.com 도메인
                    .maxAge(60 * 60) // 2주 (초 단위)
                    .sameSite("None") // SameSite 설정
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .domain(".tigleisure.com") // .tigleisure.com 도메인
                    .maxAge(14 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            // 응답 헤더에 두 개의 쿠키 추가
            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            LoginAccessTokenResponseDto loginAccessTokenResponseDto = LoginAccessTokenResponseDto.fromMember(member.getAccessToken());
            ApiResponse<LoginAccessTokenResponseDto> result = ApiResponse.of(200, "Login Success(AccessToken IN RESPONSE)", loginAccessTokenResponseDto);

            return ResponseEntity.ok(result);
        } else if ("https://tigleisure.com".equals(origin)) {
            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", member.getAccessToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .domain(".tigleisure.com") // .tigleisure.com 도메인
                    .maxAge(60 * 60)// 2주 (초 단위)
                    .sameSite("None") // SameSite 설정
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
                    .httpOnly(true)
                    .secure(true) // HTTPS 환경
                    .path("/")
                    .domain(".tigleisure.com") // .tigleisure.com 도메인
                    .maxAge(14 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();

            // 응답 헤더에 쿠키 추가
            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            ApiResponse<LoginAccessTokenResponseDto> result = ApiResponse.of(200, "Login Success(NO AccessToken IN RESPONSE)", null);
            return ResponseEntity.ok(result);
        }
        ApiResponse<LoginAccessTokenResponseDto> result = ApiResponse.of(200, "LOGIN ERROR", null);
        return ResponseEntity.ok(result);

//        // Refresh Token 쿠키 설정
//        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", member.getRefreshToken())
//                .httpOnly(true)
//                .path("/")
//                .secure(true) // HTTPS를 사용할 경우에만 true로 설정
//                .maxAge(14 * 24 * 60 * 60) // 2주
//                .sameSite("None")
//                .build();
//
//        // 쿠키를 응답 헤더에 추가
//        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
//
//        LoginAccessTokenResponseDto loginAccessTokenResponseDto = LoginAccessTokenResponseDto.fromMember(member.getAccessToken());
//        ApiResponse<LoginAccessTokenResponseDto> result = ApiResponse.of(200, "Login Success", loginAccessTokenResponseDto);
//
//        return ResponseEntity.ok(result);
    }
}
