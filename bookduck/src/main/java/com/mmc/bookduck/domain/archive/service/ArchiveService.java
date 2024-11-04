package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.ArchiveResponseDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {
    private final ExcerptService excerptService;
    private final ReviewService reviewService;

    public ArchiveResponseDto createArchive(ArchiveCreateRequestDto requestDto) {
        Excerpt savedExcerpt = null;
        Review savedReview = null;
        if (requestDto.excerptDto() != null) {
            savedExcerpt = excerptService.createExcerpt(requestDto.excerptDto());
        }
        if (requestDto.reviewDto() != null) {
            savedReview = reviewService.createReview(requestDto.reviewDto());
        }
        return ArchiveResponseDto.from(savedExcerpt, savedReview);
    }
}
