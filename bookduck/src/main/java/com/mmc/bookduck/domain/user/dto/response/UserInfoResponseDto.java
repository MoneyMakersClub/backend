package com.mmc.bookduck.domain.user.dto.response;

import com.mmc.bookduck.domain.user.dto.UserRelationshipStatusDto;
import com.mmc.bookduck.domain.user.entity.UserRelationshipStatus;

public record UserInfoResponseDto (
        String nickname,
        long bookCount,
        Boolean isOfficial,
        UserRelationshipStatus userRelationshipStatus,
        Long friendId,
        Long friendRequestId
) {
    public UserInfoResponseDto (String nickname, long bookCount, boolean isOfficial, UserRelationshipStatusDto userRelationshipStatusDto) {
        this(
                nickname,
                bookCount,
                isOfficial,
                userRelationshipStatusDto.userRelationshipStatus(),
                userRelationshipStatusDto.friendId(),
                userRelationshipStatusDto.friendRequestId()
        );
    }
}
