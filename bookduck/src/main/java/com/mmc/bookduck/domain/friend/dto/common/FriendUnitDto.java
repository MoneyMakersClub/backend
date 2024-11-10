package com.mmc.bookduck.domain.friend.dto.common;

import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;

import java.util.List;

public record FriendUnitDto(
        Long friendId, // 친구 삭제 기능
        Long userId,
        String nickname,
        List<ItemEquippedUnitDto> userItemEquipped
) {
    public static FriendUnitDto from(Friend friend, List<ItemEquippedUnitDto> userItemEquipped) {
        return new FriendUnitDto(
                friend.getFriendId(),
                friend.getUser2().getUserId(),
                friend.getUser2().getNickname(),
                userItemEquipped
        );
    }
}
