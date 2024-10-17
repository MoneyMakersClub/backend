package com.mmc.bookduck.global.security;

import com.mmc.bookduck.global.exception.CustomTokenException;
import com.mmc.bookduck.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
            try {
                jwtUtil.validateAccessToken(accessToken); // 엑세스 토큰 유효성을 검증
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증된 사용자 정보를 SecurityContext에 저장
            } catch (CustomTokenException e) {
                SecurityContextHolder.clearContext();
                // 리프레시 토큰은 유효한 경우
                if (e.getErrorCode().equals(ErrorCode.EXPIRED_ACCESS_TOKEN)){
                    ErrorResponseUtil.writeErrorResponse(response, e.getErrorCode(), request.getRequestURI(), true);
                    return;
                } else {
                    ErrorResponseUtil.writeErrorResponse(response, e.getErrorCode(), request.getRequestURI(), false);
                    return;
                }
            }
        }
        filterChain.doFilter(request,response);  // 다음 필터로 요청을 넘김
    }
}

