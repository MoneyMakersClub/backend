package com.mmc.bookduck.global.security;

import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.exception.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final Key accessKey;
    private final Key refreshKey;
    private final RedisService redisService;
    private static final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofHours(2); //2시간
    private static final Duration REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(7); //7일

    public JwtUtil(@Value("${jwt.secret.access}") String accessSecret, @Value("${jwt.secret.refresh}") String refreshSecret,
                   RedisService redisService){
        byte[] keyBytes = Decoders.BASE64.decode(accessSecret);
        this.accessKey = Keys.hmacShaKeyFor(keyBytes);
        keyBytes = Decoders.BASE64.decode(refreshSecret);
        this.refreshKey = Keys.hmacShaKeyFor(keyBytes);
        this.redisService = redisService;
    }

    public String generateAccessToken(Authentication authentication){
        return generateToken(authentication, accessKey, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generateRefreshToken(Authentication authentication){
        String refreshToken = generateToken(authentication, refreshKey, REFRESH_TOKEN_EXPIRE_TIME);

        // 리프레시 토큰을 redis에 저장
        redisService.setValuesWithTimeout(authentication.getName(), refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
        return refreshToken;
    }

    private String generateToken(Authentication authentication, Key key, Duration expiredTime){
        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());

        Date now = new Date();
        Date expireDate = new Date(now.getTime()+expiredTime.toMillis());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    // 액세스 토큰이 유효한 경우에만 에러를 던지지 않음
    public void validateAccessToken(String accessToken){
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }

    // 리프레시 토큰이 유효한 경우에만 에러를 던지지 않음
    public void validateRefreshToken(String refreshToken){
        try{
            Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(claims.get("role").toString()));
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new TokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    // 액세스 토큰 재발급에서 사용
    public boolean isTokenExpired(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken).getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e){
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public int getRefreshTokenMaxAge() {
        return (int) REFRESH_TOKEN_EXPIRE_TIME.toSeconds();
    }
}