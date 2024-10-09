package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.global.exception.CustomOAuth2AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        log.error("소셜 로그인에 실패하였습니다. 만료된 경로로 접근했을 가능성이 있습니다.");

        int status = HttpServletResponse.SC_BAD_REQUEST; // 기본 상태 코드
        String message = "소셜 로그인에 실패하였습니다."; // 기본 메시지

        // CustomOAuth2AuthenticationException인지 확인
        if (exception instanceof CustomOAuth2AuthenticationException customOAuth2AuthenticationException) {
            status = customOAuth2AuthenticationException.getErrorCode().getStatus(); // 사용자 정의 상태 코드
            message = customOAuth2AuthenticationException.getErrorCode().getMessage(); // 사용자 정의 메시지
        }

        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + message + "\", \"errorCode\": " + status + "}");
        response.getWriter().flush();
    }
}