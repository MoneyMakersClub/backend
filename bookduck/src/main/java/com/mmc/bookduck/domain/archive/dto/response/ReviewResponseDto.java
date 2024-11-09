package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.common.Visibility;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long ReviewId,
        String reviewTitle,
        String reviewContent,
        String color,
        Visibility visibility,
        LocalDateTime createdTime,
        LocalDateTime modifiedTime
) {
    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getReviewId(),
                review.getReviewTitle(),
                review.getReviewContent(),
                review.getColor(),
                review.getVisibility(),
                review.getCreatedTime(),
                review.getModifiedTime()
        );
    }
}
