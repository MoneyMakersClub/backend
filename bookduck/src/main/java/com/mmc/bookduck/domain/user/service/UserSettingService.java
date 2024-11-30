package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.book.service.BookInfoService;
import com.mmc.bookduck.domain.folder.service.FolderService;
import com.mmc.bookduck.domain.user.dto.request.UserNicknameRequestDto;
import com.mmc.bookduck.domain.user.dto.request.UserSettingUpdateRequestDto;
import com.mmc.bookduck.domain.user.dto.response.UserNicknameResponseDto;
import com.mmc.bookduck.domain.user.dto.response.UserSettingInfoResponseDto;
import com.mmc.bookduck.domain.user.dto.response.UserNicknameAvailabilityResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserSetting;
import com.mmc.bookduck.domain.user.entity.UserStatus;
import com.mmc.bookduck.domain.user.repository.UserSettingRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.security.CookieUtil;
import com.mmc.bookduck.global.security.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSettingService {
    private final UserService userService;
    private final UserSettingRepository userSettingRepository;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;
    private final FolderService folderService;
    private final BookInfoService bookInfoService;

    @Transactional(readOnly = true)
    public UserSettingInfoResponseDto getUserSettingInfo() {
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
    public UserNicknameResponseDto getUserNickname() {
        User user = userService.getCurrentUser();
        return new UserNicknameResponseDto(user.getNickname());
    }

    @Transactional(readOnly = true)
    public UserNicknameAvailabilityResponseDto checkNicknameAvailability(UserNicknameRequestDto requestDto) {
        return new UserNicknameAvailabilityResponseDto(!userService.existsByNickname(requestDto.nickname()));
    }

    public void updateUserNickname(UserNicknameRequestDto requestDto) {
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

        // null이 아닌 설정 옵션만 업데이트 진행
        updateSetting(requestDto.isPushAlarmEnabled(), userSetting::updateIsPushAlarmEnabled);
        updateSetting(requestDto.isFriendRequestEnabled(), userSetting::updateIsFriendRequestEnabled);
        updateSetting(requestDto.recordFont(), userSetting::updateRecordFont);
        userSettingRepository.save(userSetting);
    }

    // value가 null이 아닐 경우 updateFunction을 실행
    private <T> void updateSetting(T value, Consumer<T> updateFunction) {
        Optional.ofNullable(value).ifPresent(updateFunction);
    }

    public void deactivate(HttpServletResponse response) {
        User user = userService.getCurrentUser();

        // 폴더 & 커스텀책 삭제
        folderService.deleteUserFolder(user);
        bookInfoService.deleteUserCustomBook(user);

        String nickname = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        user.updateNickname(nickname);
        user.updateStatus(UserStatus.DELETED);
        userService.saveUser(user);

        // TODO: 추후 폴더 책들, 커스텀 책들, 친구 등 삭제 로직 작성

        redisService.deleteValues(user.getEmail());
        cookieUtil.deleteCookie(response, "refreshToken");
    }
}