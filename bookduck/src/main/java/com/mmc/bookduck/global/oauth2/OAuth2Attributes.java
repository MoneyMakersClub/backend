package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.domain.user.entity.LoginType;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private LoginType loginType;
    private String email;
    private boolean isFirstLogin;  // 첫 로그인 여부 필드 추가

    @Builder
    private OAuth2Attributes(String loginType, String email, boolean isFirstLogin) {
        this.loginType = LoginType.valueOf(loginType.toUpperCase());
        this.email = email;
        this.isFirstLogin = isFirstLogin;  // 첫 로그인 여부 초기화
    }

    public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(registrationId, attributes);
            case "kakao" -> ofKakao(registrationId, attributes);
            default -> throw new CustomException(ErrorCode.ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2Attributes ofGoogle(String registrationId, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .loginType(registrationId)
                .email(String.valueOf(attributes.get("email")))
                .isFirstLogin(false)  // 초기값은 false로 설정
                .build();
    }

    private static OAuth2Attributes ofKakao(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> accountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        return OAuth2Attributes.builder()
                .loginType(registrationId)
                .email(String.valueOf(accountAttributes.get("email")))
                .isFirstLogin(false)  // 초기값은 false로 설정
                .build();
    }

    // User 엔티티로 변환할 때 사용
    public User toEntity(String nickname) {
        return User.builder()
                .email(email)
                .loginType(loginType)
                .nickname(nickname)
                .build();
    }

    // 첫 로그인 여부를 설정하는 메서드
    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }
}
