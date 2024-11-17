package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Archive;

public record ArchiveResponseDto(
        Long archiveId,
        Long creatorUserId,
        ExcerptResponseDto excerpt,
        ReviewResponseDto review,
        Long bookInfoId,
        String title,
        String author,
        String imgPath
) {
    public static ArchiveResponseDto from(Archive archive, Long creatorUserId, ExcerptResponseDto excerpt, ReviewResponseDto review, Long bookInfoId, String title, String author, String imgPath) {
        return new ArchiveResponseDto(archive.getArchiveId(), creatorUserId, excerpt, review, bookInfoId, title, author, imgPath);
    }
}
