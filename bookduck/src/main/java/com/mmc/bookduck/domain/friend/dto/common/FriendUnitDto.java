package com.mmc.bookduck.domain.friend.dto.common;

import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.item.dto.common.UserItemEquippedDto;
import com.mmc.bookduck.domain.item.entity.ItemType;

import java.util.Map;

public record FriendUnitDto(
        Long friendId, // 친구 삭제 기능
        Long userId,
        String nickname,
        Map<ItemType, Long> userItemEquipped
) {
    public static FriendUnitDto from(Friend friend, Map<ItemType, Long> userItemEquipped) {
        return new FriendUnitDto(
                friend.getFriendId(),
                friend.getUser2().getUserId(),
                friend.getUser2().getNickname(),
                userItemEquipped
        );
    }
}
