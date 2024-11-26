package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.ArchiveType;
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
            ArchiveType type, // EXCERPT, REVIEW
            Object data, // ExcerptSearchUnitDto, ReviewSearchUnitDto
            String title,
            String author
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
