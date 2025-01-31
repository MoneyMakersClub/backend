package com.mmc.bookduck.domain.book.dto.request;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddUserBookRequestDto(
        @NotBlank(message = "title은 필수입니다. title은 공백일 수 없습니다.") String title,
        @NotNull(message = "author은 필수입니다.") String author,
        String imgPath
) {

    public UserBook toEntity(User user, BookInfo bookInfo, ReadStatus readStatus) {
        return UserBook.builder()
                .readStatus(readStatus)
                .user(user)
                .bookInfo(bookInfo)
                .build();
    }
}
