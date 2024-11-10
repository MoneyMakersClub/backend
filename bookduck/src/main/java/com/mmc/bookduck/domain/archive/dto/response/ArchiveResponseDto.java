package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Archive;

public record ArchiveResponseDto(
        Long archiveId,
        ExcerptResponseDto excerpt,
        ReviewResponseDto review,
        String title,
        String author
) {
    public static ArchiveResponseDto from(Archive archive, ExcerptResponseDto excerpt, ReviewResponseDto review, String title, String author) {
        return new ArchiveResponseDto(archive.getArchiveId(), excerpt, review, title, author);
    }
}
