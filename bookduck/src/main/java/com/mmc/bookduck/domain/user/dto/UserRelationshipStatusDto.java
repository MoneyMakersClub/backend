package com.mmc.bookduck.domain.user.dto;

import com.mmc.bookduck.domain.user.entity.UserRelationshipStatus;

public record UserRelationshipStatusDto(
        UserRelationshipStatus userRelationshipStatus,
        Long friendId,
        Long friendRequestId
) {
}
