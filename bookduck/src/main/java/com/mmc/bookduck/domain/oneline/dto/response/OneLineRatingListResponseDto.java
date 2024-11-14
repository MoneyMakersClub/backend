package com.mmc.bookduck.domain.oneline.dto.response;

import org.springframework.data.domain.Page;
import java.util.List;

public record OneLineRatingListResponseDto(
        int totalPages,
        int currentPage,
        int PageSize,
        long totalElements,
        List<OneLineRatingUnitDto> oneLineRatingList
) {
    public static OneLineRatingListResponseDto from(Page<OneLineRatingUnitDto> oneLineRatingPage) {
        return new OneLineRatingListResponseDto(
                oneLineRatingPage.getTotalPages(),
                oneLineRatingPage.getNumber() + 1,
                oneLineRatingPage.getSize(),
                oneLineRatingPage.getTotalElements(),
                oneLineRatingPage.getContent()
        );
    }

}