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
        System.out.println("여기 들어와졌나");
        GoogleInfoResponseDto googleMemberInfo = googleService.getGoogleMemberInfo(code);

//        GoogleInfoResponseDto googleMemberInfo = null;
//        if(origin.equals("https://localhost:3000") || origin.equals("https://localhost:8080")){
//            googleMemberInfo = googleService.getGoogleMemberInfo(code);
//        } else if(origin.equals("https://tigleisure.com")){
//            //kakaoAccessToken = googleService.getAccessTokenFromKakaoDeploy(code);
//        }
        LoginMemberResponseDto member = memberService.createGoogleMember(googleMemberInfo);

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
