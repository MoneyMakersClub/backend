package com.mmc.bookduck.global.security;

import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // Authorization 헤더에 Bearer 토큰이 있을 때만 진행
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            try {
                jwtUtil.validateAccessToken(accessToken); // 토큰 유효성을 검증
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증된 사용자 정보를 SecurityContext에 저장
            } catch (RedisConnectionFailureException | CustomException e) {
                SecurityContextHolder.clearContext();  // 보안 컨텍스트를 지우고 초기화
                request.setAttribute("exception", e instanceof RedisConnectionFailureException ? ErrorCode.REDIS_CONNECTION_ERROR : ((CustomException) e).getErrorCode());
            }
        }
        filterChain.doFilter(request,response);  // 다음 필터로 요청을 넘김
    }
}

