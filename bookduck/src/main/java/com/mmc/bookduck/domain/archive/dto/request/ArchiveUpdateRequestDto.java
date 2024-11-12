package com.mmc.bookduck.domain.archive.dto.request;

public record ArchiveUpdateRequestDto(
        ExcerptUpdateRequestDto excerpt,
        ReviewUpdateRequestDto review
) {
}