package com.mmc.bookduck.domain.user.dto.response;

import com.mmc.bookduck.domain.user.dto.common.MostReadGenreUnitDto;
import com.mmc.bookduck.domain.user.dto.common.MonthlyBookCountUnitDto;

import java.util.List;

public record UserStatisticsResponseDto(
        String nickname,
        String duckTitle,
        long bookRecordCount, // excerpt+review
        long excerptCount,
        long reviewCount,
        long finishedBookCount,
        Boolean isFirstHalf,
        List<MonthlyBookCountUnitDto> monthlyBookCount, // TODO: 추후 변수명 리팩토링
        List<MostReadGenreUnitDto> mostReadGenres,
        String mostReadAuthor,
        List<String> imgPaths
) {
}

