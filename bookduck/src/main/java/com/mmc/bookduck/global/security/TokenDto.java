package com.mmc.bookduck.global.security;

public record TokenDto(
        String accessToken,
        String refreshToken,
        int refreshTokenMaxAge
) {
    public TokenDto(String accessToken, String refreshToken){
        this(accessToken,refreshToken,7*24*60*60);
    }
}
