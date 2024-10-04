package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.global.jwt.JwtUtil;
import jakarta.servlet.ServletException;
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
    private final String REDIRECT_URL = "http://localhost:8080"; // TODO: 추후 프론트엔드 로컬 주소로 교체

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
            // accessToken 발급
            String accessToken = jwtUtil.generateAccessToken(authentication);
            // 헤더 Authorization에 Bearer Token 담기
            response.addHeader("Authorization", "Bearer " + accessToken);
            log.info("소셜 로그인에 성공하였습니다. 발급된 accessToken: " + accessToken);
            response.sendRedirect(REDIRECT_URL + "?accessToken=" + accessToken);
    }

}
