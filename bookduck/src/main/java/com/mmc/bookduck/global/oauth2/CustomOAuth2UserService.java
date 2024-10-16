package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import com.mmc.bookduck.global.exception.CustomOAuth2AuthenticationException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 제공업체의 유저 정보 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        // registrationId 가져오기 (google 또는 kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // userNameAttributeName 가져오기 (OAuth2 제공업체에서 사용자 식별을 위해 사용하는 고유 식별자의 속성명)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 유저 정보 dto 생성 (loginType, email을 DB에 저장해야)
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, oAuth2UserAttributes);

        // 회원가입 및 로그인 (User를 저장하며 가져옴)
        User user = getOrSave(oAuth2Attributes);

        // OAuth2UserDetails 반환
        return new OAuth2UserDetails(oAuth2UserAttributes, userNameAttributeName, user.getEmail(), oAuth2Attributes.isFirstLogin(), user.getRole().name());
    }

    @Transactional
    public User getOrSave(OAuth2Attributes oAuth2Attributes) {
        String email = oAuth2Attributes.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        // 해당 이메일로 가입한 유저가 존재하는지 확인
        if (user != null) {
            // 이미 존재하는 유저의 로그인 유형과 현재 로그인 유형이 다르면 에러 발생
            if (!user.getLoginType().equals(oAuth2Attributes.getLoginType())) {
                throw new CustomOAuth2AuthenticationException(ErrorCode.EMAIL_ALREADY_REGISTERED);
            }
            // 첫 로그인이 아님을 설정
            oAuth2Attributes.setFirstLogin(false);
            return user;
        } else {
            // 존재하지 않을 경우 새로운 유저 엔티티 생성. 랜덤 닉네임을 중복되지 않게 생성
            String nickname = generateUniqueNickname();
            // 첫 로그인임을 설정
            oAuth2Attributes.setFirstLogin(true);
            return userRepository.save(oAuth2Attributes.toEntity(nickname));
        }
    }

    private String generateUniqueNickname() {
        String nickname;
        do {
            nickname = "북덕" + UUID.randomUUID().toString().substring(0, 6); // 랜덤으로 생성된 6자리 UUID 사용
        } while (userRepository.existsByNickname(nickname)); // 중복 검사
        return nickname;
    }
}