package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.global.common.PaginatedResponseDto;

import java.util.List;

public record ArchiveSearchListResponseDto(
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        List<ResultWithType> archiveList
) {
    public record ResultWithType(
            String type, // EXCERPT, REVIEW
            Object data // ExcerptSearchUnitDto, ReviewSearchUnitDto
    ) {}

    public static ArchiveSearchListResponseDto from(PaginatedResponseDto<ResultWithType> paginatedResponse) {
        return new ArchiveSearchListResponseDto(
                paginatedResponse.currentPage(),
                paginatedResponse.pageSize(),
                paginatedResponse.totalElements(),
                paginatedResponse.totalPages(),
                paginatedResponse.pageContent()
        );
    }
}
