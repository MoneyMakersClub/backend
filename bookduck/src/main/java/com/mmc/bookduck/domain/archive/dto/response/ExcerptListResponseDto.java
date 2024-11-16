package com.mmc.bookduck.domain.archive.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record ExcerptListResponseDto(
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        List<ExcerptResponseDto> excerptList
) {

    public static ExcerptListResponseDto from(Page<ExcerptResponseDto> excerptPage) {
        return new ExcerptListResponseDto(
                excerptPage.getNumber(),
                excerptPage.getSize(),
                excerptPage.getTotalElements(),
                excerptPage.getTotalPages(),
                excerptPage.getContent()
        );
    }
}