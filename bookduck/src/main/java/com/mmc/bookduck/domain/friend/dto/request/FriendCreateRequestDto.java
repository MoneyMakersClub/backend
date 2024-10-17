package com.mmc.bookduck.domain.friend.dto.request;

import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.user.entity.User;

public record FriendCreateRequestDto(Long requestId, User sender, User receiver) {
    public Friend toEntity() {
        return Friend.builder()
                .user1(receiver)
                .user2(sender)
                .build();
    }
}
