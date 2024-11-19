package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Review;

public record ReviewExportResponseDto(
        String reviewTitle,
        String reviewContent,
        String color,
        String title,
        String author
) {
    public static ReviewExportResponseDto from(Review review) {
        return new ReviewExportResponseDto(
                review.getReviewTitle(),
                review.getReviewContent(),
                review.getColor(),
                review.getUserBook().getBookInfo().getTitle(),
                review.getUserBook().getBookInfo().getAuthor()
        );
    }
}
