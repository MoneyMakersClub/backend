package com.mmc.bookduck.domain.archive.dto.common;

import com.mmc.bookduck.domain.common.Visibility;

import java.time.LocalDateTime;

public record ReviewSearchUnitDto(
        Long reviewId,
        String reviewTitle,
        String reviewContent,
        Visibility visibility,
        LocalDateTime createdTime
) {
}
