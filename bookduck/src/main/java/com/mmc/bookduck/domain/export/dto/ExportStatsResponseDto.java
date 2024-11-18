package com.mmc.bookduck.domain.export.dto;

import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.user.dto.response.UserKeywordResponseDto;

import java.time.LocalDate;

public record ExportStatsResponseDto(
        String nickname,
        String season,
        LocalDate currentDate,
        long finishedBookCount,
        GenreName mostReadGenre,
        String mostReadAuthor,
        UserKeywordResponseDto keyword,
        long excerptCount,
        long reviewCount,
        long bookRecordCount // excerpt+review
) {
}
