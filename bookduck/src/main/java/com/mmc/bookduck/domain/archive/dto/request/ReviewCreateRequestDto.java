package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private String reviewTitle;
    @NotNull private String reviewContent;
    @NotNull private String color;
    @NotNull private Visibility visibility;
    @NotNull private Long userBookId;

    public void setUserBookId(Long userBookId) {
        this.userBookId = userBookId;
    }

    public Review toEntity(User user, UserBook userBook) {
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

