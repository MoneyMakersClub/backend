package com.mmc.bookduck.domain.folder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FolderRequestDto(
        @NotBlank(message = "책장 이름은 공백일 수 없습니다.")
        @Size(max = 18, message = "책장 이름은 18자를 초과할 수 없습니다.")
        String folderName) {
}