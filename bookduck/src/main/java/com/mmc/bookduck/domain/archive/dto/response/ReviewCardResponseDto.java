package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Review;

public record ReviewCardResponseDto(
        String reviewTitle,
        String reviewContent,
        String color,
        String title,
        String author
) {
    public static ReviewCardResponseDto from(Review review) {
        return new ReviewCardResponseDto(
                review.getReviewTitle(),
                review.getReviewContent(),
                review.getColor(),
                review.getUserBook().getBookInfo().getTitle(),
                review.getUserBook().getBookInfo().getAuthor()
        );
    }
}
