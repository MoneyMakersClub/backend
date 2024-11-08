package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.ArchiveResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ExcerptResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ReviewResponseDto;
import com.mmc.bookduck.domain.archive.entity.Archive;
import com.mmc.bookduck.domain.archive.entity.ArchiveType;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.archive.repository.ArchiveRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {
    private final ExcerptService excerptService;
    private final ReviewService reviewService;
    private final ArchiveRepository archiveRepository;

    public ArchiveResponseDto createArchive(ArchiveCreateRequestDto requestDto) {
        Excerpt excerpt = Optional.ofNullable(requestDto.excerpt())
                .map(excerptService::createExcerpt)
                .orElse(null);
        Review review = Optional.ofNullable(requestDto.review())
                .map(reviewService::createReview)
                .orElse(null);
        Archive archive = requestDto.toEntity(excerpt, review);
        archiveRepository.save(archive);
        return createArchiveResponseDto(archive, excerpt, review);
    }

    @Transactional(readOnly = true)
    public ArchiveResponseDto getArchive(Long id, ArchiveType archiveType) {
        Archive archive;
        if (ArchiveType.EXCERPT.equals(archiveType)) {
            archive = archiveRepository.findByExcerpt_ExcerptId(id)
                    .orElseThrow(()-> new CustomException(ErrorCode.EXCERPT_NOT_FOUND));
        } else if (ArchiveType.REVIEW.equals(archiveType)) {
            archive = archiveRepository.findByReview_ReviewId(id)
                    .orElseThrow(()->new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        } else {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
        return createArchiveResponseDto(archive, archive.getExcerpt(), archive.getReview());
    }

    public ArchiveResponseDto createArchiveResponseDto(Archive archive, Excerpt excerpt, Review review) {
        UserBook userBook;
        if (excerpt != null) {
            userBook = excerpt.getUserBook();
        } else if (review != null) {
            userBook = review.getUserBook();
        } else throw new CustomException(ErrorCode.USERBOOK_NOT_FOUND);
        String title = userBook.getBookInfo().getTitle();
        String author = userBook.getBookInfo().getAuthor();
        return ArchiveResponseDto.from(
                archive,
                excerpt != null ? ExcerptResponseDto.from(excerpt) : null,
                review != null ? ReviewResponseDto.from(review) : null,
                title,
                author
        );
    }

}
