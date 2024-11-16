package com.mmc.bookduck.domain.archive.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record UserArchiveResponseDto(
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        List<ArchiveWithType> archiveList
) {
    public record ArchiveWithType(
            String type, // EXCERPT, REVIEW
            Object data, // ExcerptResponseDto, ReviewResponseDto
            String title,
            String author
    ) {}
    public static UserArchiveResponseDto from(Page<ArchiveWithType> archivePage) {
        return new UserArchiveResponseDto(
                archivePage.getNumber(),
                archivePage.getSize(),
                archivePage.getTotalElements(),
                archivePage.getTotalPages(),
                archivePage.getContent()
        );
    }
}

