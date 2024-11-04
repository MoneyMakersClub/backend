package com.mmc.bookduck.domain.archive.dto.response;

public record ArchiveResponseDto(
        ExcerptResponseDto excerpt,
        ReviewResponseDto review,
        String title,
        String author
) {
    public static ArchiveResponseDto from(ExcerptResponseDto excerpt, ReviewResponseDto review, String title, String author) {
        return new ArchiveResponseDto(excerpt, review, title, author);
    }
}
