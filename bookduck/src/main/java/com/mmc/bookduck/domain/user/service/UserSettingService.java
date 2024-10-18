package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.user.dto.request.UserNicknameRequestDto;
import com.mmc.bookduck.domain.user.dto.request.UserSettingUpdateRequestDto;
import com.mmc.bookduck.domain.user.dto.response.UserSettingInfoResponseDto;
import com.mmc.bookduck.domain.user.dto.response.UserNicknameAvailabilityResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserSetting;
import com.mmc.bookduck.domain.user.repository.UserSettingRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSettingService {
    private final UserService userService;
    private final UserSettingRepository userSettingRepository;

    @Transactional(readOnly = true)
    public UserSettingInfoResponseDto getUserSetting() {
        User user = userService.getCurrentUser();
        UserSetting userSetting = getUserSettingByUser(user);
        return UserSettingInfoResponseDto.from(user, userSetting);
    }

    @Transactional(readOnly = true)
    public UserSetting getUserSettingByUser(User user) {
        return userSettingRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USERSETTING_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserNicknameAvailabilityResponseDto checkNicknameAvailability(UserNicknameRequestDto requestDto) {
        return new UserNicknameAvailabilityResponseDto(!userService.existsByNickname(requestDto.nickname()));
    }

    public void updateNickname(UserNicknameRequestDto requestDto) {
        String nickname = requestDto.nickname();
        User user = userService.getCurrentUser();
        boolean isAvailable = !userService.existsByNickname(nickname);
        if (isAvailable) {
            user.updateNickname(nickname); // 트랜잭션 커밋 시 자동 저장
        } else {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    public void updateOptions(UserSettingUpdateRequestDto requestDto) {
        User user = userService.getCurrentUser();
        UserSetting userSetting = getUserSettingByUser(user);
        // 트랜잭션 커밋 시 자동 저장
        userSetting.updateIsPushAlarmEnabled(requestDto.isPushAlarmEnabled());
        userSetting.updateIsFriendRequestEnabled(requestDto.isFriendRequestEnabled());
        userSetting.updateRecordFont(requestDto.recordFont());
    }
}
