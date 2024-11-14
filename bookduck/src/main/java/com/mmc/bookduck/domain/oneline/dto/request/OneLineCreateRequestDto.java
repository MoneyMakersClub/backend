package com.mmc.bookduck.domain.oneline.dto.request;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;

public record OneLineCreateRequestDto(
        @NotNull String oneLineContent,
        @NotNull Long userBookId
) {
    public OneLine toEntity(User user, UserBook userBook){
        return OneLine.builder()
                .oneLineContent(oneLineContent)
                .user(user)
                .userBook(userBook)
                .build();
    }
}
