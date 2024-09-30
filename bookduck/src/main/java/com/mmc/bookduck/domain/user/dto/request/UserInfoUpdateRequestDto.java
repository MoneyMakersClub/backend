package com.mmc.bookduck.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserInfoUpdateRequestDto (
        @NotBlank String nickname
) {
}
