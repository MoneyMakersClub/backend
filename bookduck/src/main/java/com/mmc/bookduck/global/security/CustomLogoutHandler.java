package com.mmc.bookduck.global.security;

import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getName() != null){
            // Authorization 헤더에서 JWT 토큰을 가져오기
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // 액세스 토큰이 유효한 상황 (유효하지 않으면 내부에서 예외 던짐)
                String accessToken = authHeader.substring(7);
                jwtUtil.validateAccessToken(accessToken);

                // 리프레시 토큰을 Redis에서 삭제 (authentication.getName()인 email을 key로 찾음)
                redisService.deleteValues(authentication.getName());
            }

            // 기본 로그아웃 핸들러 기능 수행
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, authentication);
        } else {
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED);
        }
    }
}