package com.mmc.bookduck.domain.archive.dto.request;

public record ArchiveCreateRequestDto(
        ExcerptCreateRequestDto excerpt,
        ReviewCreateRequestDto review
) {
}
