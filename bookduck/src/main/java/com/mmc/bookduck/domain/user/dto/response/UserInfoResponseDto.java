package com.mmc.bookduck.domain.user.dto.response;

import com.mmc.bookduck.domain.user.entity.UserRelationshipStatus;

public record UserInfoResponseDto (
        String nickname,
        long bookCount,
        Boolean isOfficial,
        UserRelationshipStatus userRelationshipStatus
) {
}
