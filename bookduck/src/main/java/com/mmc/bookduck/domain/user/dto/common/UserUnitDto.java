package com.mmc.bookduck.domain.user.dto.common;

import com.mmc.bookduck.domain.user.entity.User;

public record UserUnitDto(
        Long userId,
        String nickname
        // TODO: 캐릭터 이미지 추가해야
) {
    public static UserUnitDto from (User user) {
        return new UserUnitDto(
                user.getUserId(),
                user.getNickname()
        );
    }
}
