package com.mmc.bookduck.domain.excerpt.dto.response;

import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.excerpt.entity.Excerpt;

public record ExcerptResponseDto(
        Long excerptId,
        String excerptContent,
        Visibility visibility,
        Long pageNumber,
        Long userBookId
) {
    public static ExcerptResponseDto from(Excerpt excerpt) {
        return new ExcerptResponseDto(
                excerpt.getExcerptId(),
                excerpt.getExcerptContent(),
                excerpt.getVisibility(),
                excerpt.getPageNumber(),
                excerpt.getUserBook().getUserBookId()
        );
    }
}
