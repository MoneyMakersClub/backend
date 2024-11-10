package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class ReviewCreateRequestDto {
    @NotNull private String reviewTitle;
    @NotNull private String reviewContent;
    private String color;
    private Visibility visibility;
    private Long userBookId;

    public void setUserBookId(Long userBookId) {
        this.userBookId = userBookId;
    }

    public Review toEntity(User user, UserBook userBook, String color, Visibility visibility) {
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

