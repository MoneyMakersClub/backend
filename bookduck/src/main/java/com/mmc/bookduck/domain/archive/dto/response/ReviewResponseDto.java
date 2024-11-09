package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.common.Visibility;

public record ReviewResponseDto(
        Long ReviewId,
        String reviewTitle,
        String reviewContent,
        Visibility visibility,
        String color
) {
    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getReviewId(),
                review.getReviewTitle(),
                review.getReviewContent(),
                review.getVisibility(),
                review.getColor()
        );
    }
}
