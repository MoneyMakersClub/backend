package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;

public record ReviewCreateRequestDto(
        String title,
        String reviewContent,
        String color,
        Visibility visibility,
        Long userBookId
) {
    public Review toEntity(User user, UserBook userBook, String color) {
        return Review.builder()
                .title(title)
                .reviewContent(reviewContent)
                .color(color)
                .visibility(visibility)
                .user(user)
                .userBook(userBook)
                .build();
    }
}
