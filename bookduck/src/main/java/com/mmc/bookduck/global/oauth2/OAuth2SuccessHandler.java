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
    private static final String OAUTH_PATH = "/api/oauth";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            // 요청의 origin 또는 referer 헤더를 사용해 출처 확인
            String origin = request.getHeader("origin");
            String referer = request.getHeader("referer");

            // origin이나 referer 헤더가 있는 경우 이를 기준으로 redirectUrl 설정
            String baseRedirectUrl;
            if (origin != null) {
                baseRedirectUrl = origin;
            } else if (referer != null) {
                baseRedirectUrl = referer.split("/")[0] + "//" + referer.split("/")[2]; // 프로토콜 및 호스트만 사용
            } else {
                // 기본값을 로컬호스트로 설정
                baseRedirectUrl = "http://localhost:3000";
            }
            String redirectUrl = baseRedirectUrl + OAUTH_PATH;

            // 액세스 토큰 및 리프레시 토큰 발급, 리프레시 토큰을 쿠키에 저장
            String accessToken = jwtUtil.generateAccessToken(authentication);
            int expiresIn = jwtUtil.getAccessTokenMaxAge();
            String refreshToken = jwtUtil.generateRefreshToken(authentication);
            cookieUtil.addCookie(response, "refreshToken", refreshToken, jwtUtil.getRefreshTokenMaxAge());

            // OAuth2UserDetails으로부터 신규 유저 여부와 userId 확인
            OAuth2UserDetails userDetails = (OAuth2UserDetails) authentication.getPrincipal();
            boolean isNewUser = userDetails.isNewUser();
            Long userId = userDetails.userId();

            log.info("소셜 로그인 {}에 성공하였습니다. 발급된 accessToken: {}", isNewUser ? "회원 가입" : "로그인", accessToken);

            // 액세스 토큰을 쿼리 파라미터로 전달하여 리디렉션
            String redirectUrlWithParams = UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("accessToken", accessToken)
                    .queryParam("expiresIn", expiresIn)
                    .queryParam("isNewUser", isNewUser)
                    .queryParam("userId", userId)
                    .build()
                    .toUriString();
            response.sendRedirect(redirectUrlWithParams);
        } catch (Exception e) {
            log.error("소셜 로그인 실패: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "소셜 로그인 실패");
        }
    }
}
