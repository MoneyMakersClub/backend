package com.mmc.bookduck.domain.excerpt.dto.request;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.excerpt.entity.Excerpt;
import com.mmc.bookduck.domain.user.entity.User;

public record ExcerptSaveRequestDto(
        String excerptContent,
        Visibility visibility,
        Long pageNumber,
        String color,
        Long userBookId
) {
    public Excerpt toEntity(User user, UserBook userBook) {
        return new Excerpt(excerptContent, visibility, pageNumber, color, user, userBook);
    }
}
