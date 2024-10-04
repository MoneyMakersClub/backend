package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        // 2. registrationId 가져오기 (third-party id)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. userNameAttributeName 가져오기.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 4. 유저 정보 dto 생성
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.ofKakao(registrationId, oAuth2UserAttributes);

        // 5. 회원가입 및 로그인
        User user = getOrSave(oAuth2Attributes);

        // 6. AuthDetails 로 반환
        log.info("CustomOAuth2UserService - loadUser() : OAuth2UserDetails로 반환합니다.");
        return new OAuth2UserDetails(oAuth2UserAttributes, userNameAttributeName, oAuth2Attributes.getOauth2UserEmail());
    }

    private User getOrSave(OAuth2Attributes oAuth2Attributes) { // provider도 확인해야 함
        User user = userRepository.findByEmail(oAuth2Attributes.getOauth2UserEmail())
                .orElseGet(oAuth2Attributes::toEntity);
        return userRepository.save(user);
    }
}