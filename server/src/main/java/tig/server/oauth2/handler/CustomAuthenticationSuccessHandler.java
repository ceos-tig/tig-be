package tig.server.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tig.server.jwt.TokenProvider;
import tig.server.member.service.MemberService;
import tig.server.oauth2.PrincipalDetails;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String username = principalDetails.getName();

        String provider = "kakao"; // 일단 카카오만 있기 때문에,,
        String providerId = principalDetails.getAttributes().get("id").toString();
        String uniqueId = provider + "_" + providerId;

        String accessToken = tokenProvider.createAccessToken(username, uniqueId, authResult);
        String refreshToken = tokenProvider.createRefreshToken(username, uniqueId);

        memberService.saveOrUpdateRefreshToken(uniqueId, refreshToken);

        // Access Token 쿠키 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true) // HTTPS를 사용할 경우에만 true로 설정
                .maxAge(24 * 60 * 60) // 24시간
                .sameSite("None")
                .build();

        // Refresh Token 쿠키 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // HTTPS를 사용할 경우에만 true로 설정
                .maxAge(14 * 24 * 60 * 60) // 2주
                .sameSite("None")
                .build();

        // 쿠키를 응답 헤더에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        log.info("Set-Cookie headers: {}", response.getHeaders(HttpHeaders.SET_COOKIE));
        log.info("Response headers: {}", response.getHeaderNames());

        // CORS 설정을 위해 헤더 추가
        response.addHeader("Access-Control-Allow-Origin", "https://main--testtig.netlify.app/");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

        response.sendRedirect("https://main--testtig.netlify.app/");
    }
}
