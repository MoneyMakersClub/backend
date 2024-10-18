package com.mmc.bookduck.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserNicknameRequestDto(
        @NotBlank(message="닉네임은 비워둘 수 없습니다.") String nickname
) {
}
