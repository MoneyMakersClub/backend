package com.mmc.bookduck.domain.friend.dto.common;

import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;
import com.mmc.bookduck.domain.user.entity.User;

import java.util.List;

public record FriendUnitDto(
        Long friendId, // 친구 삭제 기능
        Long userId,
        String nickname,
        Boolean isOfficial,
        List<ItemEquippedUnitDto> userItemEquipped
) {
    public static FriendUnitDto from(Friend friend, User friendUser, Boolean isOfficial, List<ItemEquippedUnitDto> userItemEquipped) {
        return new FriendUnitDto(
                friend.getFriendId(),
                friendUser.getUserId(),
                friendUser.getNickname(),
                isOfficial,
                userItemEquipped
        );
    }
}
