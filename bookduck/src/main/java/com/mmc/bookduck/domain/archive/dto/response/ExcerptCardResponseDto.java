package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Excerpt;

public record ExcerptCardResponseDto(
        String excerptContent,
        Long pageNumber,
        String title,
        String author
) {
    public static ExcerptCardResponseDto from(Excerpt excerpt) {
        return new ExcerptCardResponseDto(
                excerpt.getExcerptContent(),
                excerpt.getPageNumber(),
                excerpt.getUserBook().getBookInfo().getTitle(),
                excerpt.getUserBook().getBookInfo().getAuthor()
        );
    }
}
