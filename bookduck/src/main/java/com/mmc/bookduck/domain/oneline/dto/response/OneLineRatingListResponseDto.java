package com.mmc.bookduck.domain.oneline.dto.response;

import org.springframework.data.domain.Page;
import java.util.List;

public record OneLineRatingListResponseDto(
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        Long bookInfoId,
        List<OneLineRatingUnitDto> oneLineRatingList
) {
    public static OneLineRatingListResponseDto from(Long bookInfoId, Page<OneLineRatingUnitDto> oneLineRatingPage) {
        return new OneLineRatingListResponseDto(
                oneLineRatingPage.getNumber(),
                oneLineRatingPage.getSize(),
                oneLineRatingPage.getTotalElements(),
                oneLineRatingPage.getTotalPages(),
                bookInfoId,
                oneLineRatingPage.getContent()
        );
    }

}