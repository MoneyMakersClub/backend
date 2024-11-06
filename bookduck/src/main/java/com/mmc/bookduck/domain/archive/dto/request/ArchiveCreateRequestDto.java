package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.archive.entity.Archive;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;

public record ArchiveCreateRequestDto(
        ExcerptCreateRequestDto excerpt,
        ReviewCreateRequestDto review
) {

    public Archive toEntity(Excerpt excerpt, Review review) {
        return Archive.builder()
                .excerpt(excerpt)
                .review(review)
                .build();
    }
}
