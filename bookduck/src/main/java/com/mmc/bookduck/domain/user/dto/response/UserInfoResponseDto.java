package com.mmc.bookduck.domain.user.dto.response;

public record UserInfoResponseDto (
        String nickname,
        long bookCount,
        Boolean isOfficial,
        Boolean isFriend
) {
}
