package com.mmc.bookduck.domain.archive.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record ExcerptSearchResponseDto (
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        List<ExcerptResponseDto> excerptList
) {

    public static ExcerptSearchResponseDto from(Page<ExcerptResponseDto> excerptPage) {
        return new ExcerptSearchResponseDto(
                excerptPage.getNumber(),
                excerptPage.getSize(),
                excerptPage.getTotalElements(),
                excerptPage.getTotalPages(),
                excerptPage.getContent()
        );
    }
}