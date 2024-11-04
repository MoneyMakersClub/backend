package com.mmc.bookduck.domain.archive.dto.request;

public record ArchiveCreateRequestDto(
        ExcerptCreateRequestDto excerptDto,
        ReviewCreateRequestDto reviewDto
) {
}
