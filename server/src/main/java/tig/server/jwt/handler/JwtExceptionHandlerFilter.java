package tig.server.jwt.handler;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tig.server.error.BusinessExceptionHandler;
import tig.server.error.ErrorCode;
import tig.server.jwt.TokenProvider;
import tig.server.member.service.MemberDetailsServiceImpl;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final MemberDetailsServiceImpl memberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = resolveToken(servletRequest);
            System.out.println("jwt access token = " + jwt);
            //만약 토큰에 이상이 있다면 오류가 발생한다.
            if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
                //tokenProvider에서 jwt를 가져가 Authentication 객체생성
                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            //이상이 없다면 계속 진행
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (JwtException e) {
            //토큰에 오류가 있다면 401에러를 응답한다.
            log.error("[JWTExceptionHandlerFilter] "+e.getMessage());
            servletResponse.setStatus(401);
            servletResponse.setContentType("application/json;charset=UTF-8");
        }
    }

    private void handleAuthenticationException(HttpServletResponse response, AuthenticationException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized - " + ex.getMessage() + "\"}");
    }

    private void handleOtherExceptions(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Error processing request - " + ex.getMessage() + "\"}");
    }

    private String resolveToken(HttpServletRequest request) {
        // 헤더에서 토큰 추출
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && StringUtils.startsWithIgnoreCase(bearerToken, "Bearer ")) {
            System.out.println("bearerToken = " + bearerToken);
            return bearerToken.substring(7);
        }
        // 쿠키에서 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("cookies.length = " + cookies.length);
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    System.out.println("cookie.getName() = " + cookie.getName());
                    System.out.println("cookie.getValue() = " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }

        throw new BusinessExceptionHandler("No AccessToken Found", ErrorCode.BAD_REQUEST_ERROR);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String uniqueId) {
        System.out.println("username hihi = " + uniqueId);
        UserDetails userDetails = memberDetailsService.loadUserByUsername(uniqueId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
