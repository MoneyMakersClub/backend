package com.mmc.bookduck.domain.userhome.dto.common;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.common.Visibility;

public record ExcerptWithBookInfoUnitDto (
        Long excerptId,
        String excerptContent,
        Long pageNumber,
        Visibility visibility,
        String title,
        String author
) {
    public ExcerptWithBookInfoUnitDto(Excerpt excerpt) {
        this(
                excerpt.getExcerptId(),
                excerpt.getExcerptContent(),
                excerpt.getPageNumber(),
                excerpt.getVisibility(),
                excerpt.getUserBook().getBookInfo().getTitle(),
                excerpt.getUserBook().getBookInfo().getAuthor()
        );
    }
}
