package com.mmc.bookduck.domain.friend.dto.common;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.item.dto.common.UserItemEquippedDto;
import com.mmc.bookduck.domain.item.entity.ItemType;

import java.util.Map;

public record FriendRequestUnitDto(
        Long requestId,
        Long userId,
        String userNickname,
        Map<ItemType, Long> userItemEquipped
) {
    public static FriendRequestUnitDto from(FriendRequest friendRequest, Long userId, String userNickname, Map<ItemType, Long> userItemEquipped) {
        return new FriendRequestUnitDto(
                friendRequest.getRequestId(),
                userId,
                userNickname,
                userItemEquipped
        );
    }
}
