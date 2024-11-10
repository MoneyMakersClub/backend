package com.mmc.bookduck.domain.user.dto.response;

import com.mmc.bookduck.domain.user.entity.LoginType;
import com.mmc.bookduck.domain.user.entity.RecordFont;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserSetting;

public record UserSettingInfoResponseDto(
        String nickname,
        LoginType loginType,
        String email,
        Boolean isPushAlarmEnabled,
        Boolean isFriendRequestEnabled,
        RecordFont recordFont
) {
    public static UserSettingInfoResponseDto from(User user, UserSetting userSetting) {
        return new UserSettingInfoResponseDto(
                user.getNickname(),
                user.getLoginType(),
                user.getEmail(),
                userSetting.isPushAlarmEnabled(),
                userSetting.isFriendRequestEnabled(),
                userSetting.getRecordFont()
        );
    }
}
