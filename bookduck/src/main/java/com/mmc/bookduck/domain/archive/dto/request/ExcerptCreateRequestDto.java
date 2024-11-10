package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExcerptCreateRequestDto {
    @NotNull private String excerptContent;
    @NotNull private Long pageNumber;
    private Visibility visibility;
    private Long userBookId;

    public void setUserBookId(Long userBookId) {
        this.userBookId = userBookId;
    }

    public Excerpt toEntity(User user, UserBook userBook, boolean isMain, Visibility visibility) {
        return Excerpt.builder()
                .excerptContent(excerptContent)
                .visibility(visibility)
                .isMain(isMain)
                .pageNumber(pageNumber)
                .user(user)
                .userBook(userBook)
                .build();
    }
}
