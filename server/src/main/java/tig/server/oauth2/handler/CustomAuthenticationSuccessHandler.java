package tig.server.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import tig.server.enums.MemberRoleEnum;
import tig.server.jwt.TokenProvider;
import tig.server.member.MemberDetailsImpl;
import tig.server.oauth2.PrincipalDetails;
import tig.server.oauth2.memberInfo.OAuth2MemberInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String username = principalDetails.getName();
        String accessToken = tokenProvider.createAccessToken(username, authResult);
        String refreshToken = tokenProvider.createRefreshToken(username);

        // Access Token 쿠키 설정
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        //accessTokenCookie.setSecure(true); // HTTPS를 사용할 경우에만 true로 설정
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(24 * 60 * 60); // 24시간

        // Refresh Token 쿠키 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        //refreshTokenCookie.setSecure(true); // HTTPS를 사용할 경우에만 true로 설정
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(14 * 24 * 60 * 60); // 2주

        // 쿠키를 응답 헤더에 추가
        response.addHeader("Set-Cookie", createSetCookieHeader(accessTokenCookie));
        response.addHeader("Set-Cookie", createSetCookieHeader(refreshTokenCookie));

        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }

    private String createSetCookieHeader(Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        sb.append("Max-Age=").append(cookie.getMaxAge()).append(";");
        sb.append("Path=").append(cookie.getPath()).append(";");
        if (cookie.isHttpOnly()) {
            sb.append("HttpOnly;");
        }
        if (cookie.getSecure()) { // https 뚫으면 SameSite=None; Secure; 설정해주어야 함.
            sb.append("Secure;");
            sb.append("SameSite=None");
        }
        return sb.toString();
    }
}
