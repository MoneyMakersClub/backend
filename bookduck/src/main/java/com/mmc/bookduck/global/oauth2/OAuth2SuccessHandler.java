package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.global.security.CookieUtil;
import com.mmc.bookduck.global.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private static final String REDIRECT_URL = "http://localhost:3000/home"; // TODO: 추후 프론트엔드 배포 주소로 교체

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            // 새로운 액세스 토큰 발급
            String accessToken = jwtUtil.generateAccessToken(authentication);
            // 액세스 토큰 유효 시간
            int expiresIn = jwtUtil.getAccessTokenMaxAge();

             // 새로운 리프레시 토큰 발급
            String refreshToken = jwtUtil.generateRefreshToken(authentication);
            // 리프레시 토큰을 HttpOnly 쿠키에 저장
            cookieUtil.addCookie(response, "refreshToken", refreshToken, jwtUtil.getRefreshTokenMaxAge());

            // OAuth2UserDetails를 통해 신규 유저 여부 확인
            OAuth2UserDetails userDetails = (OAuth2UserDetails) authentication.getPrincipal();
            boolean isNewUser = userDetails.isNewUser();

            // 로그 출력용
            if (isNewUser) {
                log.info("소셜 로그인-회원 가입에 성공하였습니다. 발급된 accessToken: {}", accessToken);
            } else {
                log.info("소셜 로그인-가입된 계정으로 로그인 성공하였습니다. 발급된 accessToken: {}", accessToken);
            }

            // 액세스 토큰을 쿼리 파라미터로 전달하여 리디렉션
            String redirectUrl = UriComponentsBuilder.fromUriString(REDIRECT_URL)
                    .queryParam("accessToken", accessToken)
                    .queryParam("expiresIn", expiresIn)
                    .queryParam("isNewUser", isNewUser)
                    .build()
                    .toUriString();
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("소셜 로그인 실패: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "소셜 로그인 실패");
        }
    }
}
