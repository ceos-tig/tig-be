package tig.server.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
