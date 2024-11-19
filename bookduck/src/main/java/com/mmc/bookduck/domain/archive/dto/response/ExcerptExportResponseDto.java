package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Excerpt;

public record ExcerptExportResponseDto(
        String excerptContent,
        Long pageNumber,
        String title,
        String author
) {
    public static ExcerptExportResponseDto from(Excerpt excerpt) {
        return new ExcerptExportResponseDto(
                excerpt.getExcerptContent(),
                excerpt.getPageNumber(),
                excerpt.getUserBook().getBookInfo().getTitle(),
                excerpt.getUserBook().getBookInfo().getAuthor()
        );
    }
}
