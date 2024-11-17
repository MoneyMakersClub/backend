package com.mmc.bookduck.domain.book.dto.request;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AddUserBookRequestDto(
        @NotBlank(message = "title은 필수입니다. title은 공백일 수 없습니다.") String title,
        @NotEmpty(message = "authors은 필수입니다. authors은 최소 한 개 이상의 요소를 포함해야 합니다.") List<String> authors,
        @NotBlank(message = "readStatus는 필수입니다.") String readStatus,
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
