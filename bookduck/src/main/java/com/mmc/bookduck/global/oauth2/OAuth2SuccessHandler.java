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
    private static final String FIRST_LOGIN_REDIRECT_URL  = "http://localhost:3000/welcome"; // 첫 로그인(가입) 시 리디렉션할 URL

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            // 새로운 액세스 토큰 발급
            String accessToken = jwtUtil.generateAccessToken(authentication);
             // 새로운 리프레시 토큰 발급
            String refreshToken = jwtUtil.generateRefreshToken(authentication);

            // 리프레시 토큰을 HttpOnly 쿠키에 저장
            cookieUtil.addCookie(response, "refreshToken", refreshToken, jwtUtil.getRefreshTokenMaxAge());

            // OAuth2UserDetails를 통해 첫 로그인 여부 확인
            OAuth2UserDetails userDetails = (OAuth2UserDetails) authentication.getPrincipal();
            boolean isFirstLogin = userDetails.isFirstLogin();

            // 첫 로그인 여부에 따라 리디렉션 URL 다르게 설정
            String redirectUrl = isFirstLogin ? FIRST_LOGIN_REDIRECT_URL : REDIRECT_URL;

            // 로그 출력용
            if (isFirstLogin) {
                log.info("소셜 로그인-회원 가입에 성공하였습니다. 발급된 accessToken: {}", accessToken);
            } else {
                log.info("소셜 로그인-가입된 계정으로 로그인 성공하였습니다. 발급된 accessToken: {}", accessToken);
            }

            // 액세스 토큰을 쿼리 파라미터로 전달하여 리디렉션
            response.sendRedirect(redirectUrl + "?accessToken=" + accessToken);
        } catch (Exception e) {
            log.error("소셜 로그인 실패: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "소셜 로그인 실패");
        }
    }
}
