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
    private String loginType;
    private String email;

    @Builder
    private OAuth2Attributes(String loginType, String email) {
        this.loginType = loginType;
        this.email = email;
    }

    public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(registrationId, attributes);
            case "kakao" -> ofKakao(registrationId, attributes);
            default -> throw new CustomException(ErrorCode.ILLEGAL_REGISTRATION_ID);
        };
    }

    private  static OAuth2Attributes ofGoogle(String registrationId, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .loginType(registrationId)
                .email(String.valueOf(attributes.get("email")))
                .build();
    }

    private static OAuth2Attributes ofKakao(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> accountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        return OAuth2Attributes.builder()
                .loginType(registrationId)
                .email(String.valueOf(accountAttributes.get("email")))
                .build();
    }

    // User 엔티티로 변환할 때 사용
    public User toEntity(String nickname) {
        return User.builder()
                .email(email)
                .loginType(LoginType.valueOf(loginType.toUpperCase()))
                .nickname(nickname)
                .build();
    }
}
