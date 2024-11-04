package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequestDto(
        @NotNull String reviewTitle,
        @NotNull String reviewContent,
        String color,
        Visibility visibility,
        @NotNull Long userBookId
) {
    public Review toEntity(User user, UserBook userBook, String color) {
        return Review.builder()
                .reviewTitle(reviewTitle)
                .reviewContent(reviewContent)
                .color(color)
                .visibility(visibility)
                .user(user)
                .userBook(userBook)
                .build();
    }
}
