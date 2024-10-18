package com.mmc.bookduck.global.oauth2;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserGrowth;
import com.mmc.bookduck.domain.user.entity.UserHome;
import com.mmc.bookduck.domain.user.entity.UserSetting;
import com.mmc.bookduck.domain.user.repository.UserGrowthRepository;
import com.mmc.bookduck.domain.user.repository.UserHomeRepository;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import com.mmc.bookduck.domain.user.repository.UserSettingRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserSettingRepository userSettingRepository;
    private final UserHomeRepository userHomeRepository;
    private final UserGrowthRepository userGrowthRepository;

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
        User emailUser = userRepository.findByEmail(email).orElse(null);

        // 해당 이메일로 가입한 유저가 존재하는지 확인
        if (emailUser != null) {
            // 이미 존재하는 유저의 로그인 유형과 현재 로그인 유형이 다르면 에러 발생
            if (!emailUser.getLoginType().equals(oAuth2Attributes.getLoginType())) {
                throw new CustomOAuth2AuthenticationException(ErrorCode.EMAIL_ALREADY_REGISTERED);
            }
            oAuth2Attributes.setFirstLogin(false);
            return emailUser;
        } else {
            // 존재하지 않을 경우 새로운 유저 엔티티 생성. 랜덤 닉네임을 중복되지 않게 생성
            String nickname = generateUniqueNickname();
            oAuth2Attributes.setFirstLogin(true);

            // 새로운 User 생성
            User newUser = oAuth2Attributes.toEntity(nickname);

            // 새로운 User를 먼저 저장
            newUser = userRepository.save(newUser);

            // 새로운 User의 UserSetting, UserHome, UserGrowth 생성
            UserSetting userSetting = UserSetting.builder().user(newUser).build();
            UserHome userHome = UserHome.builder().user(newUser).build();
            UserGrowth userGrowth = UserGrowth.builder().user(newUser).build();

            // 각 Repository에 저장
            userSettingRepository.save(userSetting);
            userHomeRepository.save(userHome);
            userGrowthRepository.save(userGrowth);

            return newUser;
        }
    }

    private String generateUniqueNickname() {
        final int maxNicknameLength = 8;
        String[] prefixes = {
                "행복한", "즐거운", "상냥한", "발랄한", "귀여운",
                "용감한", "따뜻한", "활기찬", "신나는", "멋쟁이",
                "웃는", "맑은", "포근", "튼튼", "순수",
                "조용", "멋진", "상쾌", "활발", "빛나는",
                "기쁜", "부끄", "새침", "춤추는", "반짝"
        };
        String suffixChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String prefix = prefixes[(int) (Math.random() * prefixes.length)];
        String nickname;
        int suffixLength = maxNicknameLength - prefix.length() - 2;

        do {
            StringBuilder suffix = new StringBuilder();
            // suffixChars에서 랜덤 글자를 선택
            for (int i = 0; i < suffixLength; i++) {
                char randomChar = suffixChars.charAt((int) (Math.random() * suffixChars.length()));
                suffix.append(randomChar);
            }
            // 닉네임 선택 및 8글자로 자르기
            nickname = prefix + "북덕" + suffix;
            nickname = nickname.substring(0, 8);
        } while (userRepository.existsByNickname(nickname)); // 중복 검사

        return nickname;
    }
}