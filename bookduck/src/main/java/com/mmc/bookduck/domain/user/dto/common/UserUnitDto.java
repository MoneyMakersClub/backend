package com.mmc.bookduck.domain.user.dto.common;

import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;
import com.mmc.bookduck.domain.user.entity.User;

import java.util.List;

public record UserUnitDto(
        Long userId,
        String nickname,
        boolean isFriend,
        List<ItemEquippedUnitDto> itemEquipped
) {
    public static UserUnitDto from (User user, List<ItemEquippedUnitDto> itemEquipped, boolean isFriend) {
        return new UserUnitDto(
                user.getUserId(),
                user.getNickname(),
                isFriend,
                itemEquipped
        );
    }
}
