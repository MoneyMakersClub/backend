package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.archive.dto.response.ExcerptResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ReviewResponseDto;
import com.mmc.bookduck.domain.archive.entity.ArchiveType;

public record ReviewExcerptUnitDto(ArchiveType archiveType,
                                   ExcerptResponseDto excerpt,
                                   ReviewResponseDto review
                                   )
{
    public static ReviewExcerptUnitDto from(ExcerptResponseDto excerptDto){
        return new ReviewExcerptUnitDto(
                ArchiveType.EXCERPT,
                excerptDto,
                null
        );
    }
    public static ReviewExcerptUnitDto from(ReviewResponseDto reviewDto){
        return new ReviewExcerptUnitDto(
                ArchiveType.REVIEW,
                null,
                reviewDto
        );
    }
}
