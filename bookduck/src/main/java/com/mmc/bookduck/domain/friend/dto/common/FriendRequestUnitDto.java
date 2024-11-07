package com.mmc.bookduck.domain.friend.dto.common;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.item.dto.common.UserItemEquippedDto;

public record FriendRequestUnitDto(
        Long requestId,
        Long userId,
        String userNickname,
        UserItemEquippedDto userItemEquipped
) {
    public static FriendRequestUnitDto from(FriendRequest friendRequest, Long userId, String userNickname, UserItemEquippedDto userItemEquipped) {
        return new FriendRequestUnitDto(
                friendRequest.getRequestId(),
                userId,
                userNickname,
                userItemEquipped
        );
    }
}
