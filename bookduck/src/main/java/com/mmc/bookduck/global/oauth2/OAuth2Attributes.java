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
    private String oauth2UserEmail;
    private String birth;
    private String gender;
    private String country;

    @Builder
    private OAuth2Attributes(String loginType, String oauth2UserEmail, String birth, String gender, String country) {
        this.loginType = loginType;
        this.oauth2UserEmail = oauth2UserEmail;
        this.birth = birth;
        this.gender = gender;
        this.country = country;
    }

    public static OAuth2Attributes ofKakao(String registrationId, Map<String, Object> attributes) {
        if (!registrationId.equals("kakao"))
            throw new CustomException(ErrorCode.ILLEGAL_REGISTRATION_ID);
        Map<String, Object> accountAttributes = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2Attributes.builder()
                .loginType(registrationId)
                .oauth2UserEmail(String.valueOf(accountAttributes.get("email")))
                .birth(String.valueOf(accountAttributes.get("birth")))
                .gender(String.valueOf(accountAttributes.get("gender")))
                .country(String.valueOf(accountAttributes.get("country")))
                .build();
    }

    public User toEntity() {
        return User.builder()
                .loginType(LoginType.valueOf(loginType))
                .email(oauth2UserEmail)
                .birth(birth)
                .gender(gender)
                .country(country)
                .build();
    }
}
