package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.global.security.CookieUtil;
import com.mmc.bookduck.global.security.JwtUtil;
import com.mmc.bookduck.global.security.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private static final String REDIRECT_URL = "http://localhost:3000"; // TODO: 추후 프론트엔드 로컬 주소로 교체

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            // 새로운 액세스 토큰 발급
            String accessToken = jwtUtil.generateAccessToken(authentication);
             // 새로운 리프레시 토큰 발급
            String refreshToken = jwtUtil.generateRefreshToken(authentication);
            log.info("토큰들 발급 완료");

            // 리프레시 토큰을 HttpOnly 쿠키에 저장
            cookieUtil.addCookie(response, "refreshToken", refreshToken, jwtUtil.getRefreshTokenMaxAge());

            // 액세스 토큰을 쿼리 파라미터로 전달하여 리디렉션
            log.info("소셜 로그인에 성공하였습니다. 발급된 accessToken: " + accessToken);
            response.sendRedirect(REDIRECT_URL + "?accessToken=" + accessToken);
        } catch (Exception e) {
            log.error("소셜 로그인 실패: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "소셜 로그인 실패");
        }
    }
}
