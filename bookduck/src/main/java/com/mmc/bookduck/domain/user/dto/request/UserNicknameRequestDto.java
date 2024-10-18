package com.mmc.bookduck.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserNicknameRequestDto(
        @NotBlank(message = "닉네임은 비워둘 수 없습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]*$", message = "닉네임에는 특수문자나 띄어쓰기를 포함할 수 없습니다.")
        @Size(max = 8, message = "닉네임은 최대 8자까지 입력할 수 있습니다.")
        String nickname
) {
}
