package com.mmc.bookduck.global.jwt;

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

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            String accessToken = authorizationHeader.substring(7);

            try{
                if(jwtUtil.validateAccessToken(accessToken)){
                    Authentication authentication = jwtUtil.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (RedisConnectionFailureException e){
                SecurityContextHolder.clearContext();
                request.setAttribute("exception", ErrorCode.REDIS_CONNECTION_ERROR);
            } catch (CustomException e){
                request.setAttribute("exception",e.getErrorCode());
            }
        }
        filterChain.doFilter(request,response);
    }
}

