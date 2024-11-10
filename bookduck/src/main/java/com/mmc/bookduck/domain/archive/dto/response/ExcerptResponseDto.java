package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.archive.entity.Excerpt;

import java.time.LocalDateTime;

public record ExcerptResponseDto(
        Long excerptId,
        String excerptContent,
        Long pageNumber,
        Visibility visibility,
        LocalDateTime createdTime,
        LocalDateTime modifiedTime
) {
    public static ExcerptResponseDto from(Excerpt excerpt) {
        return new ExcerptResponseDto(
                excerpt.getExcerptId(),
                excerpt.getExcerptContent(),
                excerpt.getPageNumber(),
                excerpt.getVisibility(),
                excerpt.getCreatedTime(),
                excerpt.getModifiedTime()
        );
    }
}
