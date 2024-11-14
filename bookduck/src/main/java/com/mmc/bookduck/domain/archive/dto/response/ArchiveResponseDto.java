package com.mmc.bookduck.domain.archive.dto.response;

import com.mmc.bookduck.domain.archive.entity.Archive;

public record ArchiveResponseDto(
        Long archiveId,
        ExcerptResponseDto excerpt,
        ReviewResponseDto review,
        Long bookInfoId,
        String title,
        String author
        // ToDo: 디자인 수정사항 보고 응답 수정
        // 책커버
        // 출판사?
        // 출판년도?
) {
    public static ArchiveResponseDto from(Archive archive, ExcerptResponseDto excerpt, ReviewResponseDto review, Long bookInfoId, String title, String author) {
        return new ArchiveResponseDto(archive.getArchiveId(), excerpt, review, bookInfoId, title, author);
    }
}
