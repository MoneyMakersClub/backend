package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.ArchiveResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ExcerptResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ReviewResponseDto;
import com.mmc.bookduck.domain.archive.entity.Archive;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.archive.repository.ArchiveRepository;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.IllegalFormatException;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {
    private final ExcerptService excerptService;
    private final ReviewService reviewService;
    private final ArchiveRepository archiveRepository;

    public ArchiveResponseDto createArchive(ArchiveCreateRequestDto requestDto) {
        Excerpt excerpt = null;
        Review review = null;
        if (requestDto.excerpt() != null) {
            excerpt = excerptService.createExcerpt(requestDto.excerpt());
        }
        if (requestDto.review() != null) {
            review = reviewService.createReview(requestDto.review());
        }
        if (excerpt != null && review != null) {
            Archive archive = requestDto.toEntity(excerpt, review);
            archiveRepository.save(archive);
        }
        return createArchiveResponseDto(excerpt, review);
    }

    public ArchiveResponseDto getArchive(Long id, String type) {
        Archive archive = null;

        if ("excerpt".equals(type)) {
            archive = archiveRepository.findByExcerptId(id).orElse(null);
        } else if ("review".equals(type)) {
            archive = archiveRepository.findByReviewId(id).orElse(null);
        }

        if (archive != null) {
            return createArchiveResponseDto(archive.getExcerpt(), archive.getReview());
        } else {
            // archive가 없을 경우 type에 따라 excerpt나 review만 반환
            return switch (type) {
                case "excerpt" -> createArchiveResponseDto(excerptService.getExcerptById(id), null);
                case "review" -> createArchiveResponseDto(null, reviewService.getReviewById(id));
                default -> throw new CustomException(ErrorCode.ERROR);
            };
        }
    }

    public ArchiveResponseDto createArchiveResponseDto(Excerpt excerpt, Review review) {
        String title = (excerpt != null ? ((Excerpt) excerpt).getUserBook() : ((Review) review).getUserBook()).getBookInfo().getTitle();
        String author = (excerpt != null ? ((Excerpt) excerpt).getUserBook() : ((Review) review).getUserBook()).getBookInfo().getAuthor();
        return ArchiveResponseDto.from(
                excerpt != null ? ExcerptResponseDto.from(excerpt) : null,
                review != null ? ReviewResponseDto.from(review) : null,
                title,
                author
        );
    }

}
