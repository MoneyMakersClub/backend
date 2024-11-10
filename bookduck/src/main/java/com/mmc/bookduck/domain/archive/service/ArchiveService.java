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
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
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
    private final UserService userService;
    private final UserBookService userBookService;
    private final ArchiveRepository archiveRepository;

    public ArchiveResponseDto createArchive(ArchiveCreateRequestDto requestDto) {
        // UserBook 결정(createExcerpt,Review의 findById때문에)
        UserBook userBook = userBookService.getUserBookOrAdd(null, null, requestDto);
        // Excerpt 생성 시 결정된 UserBook 사용
        Excerpt excerpt = Optional.ofNullable(requestDto.getExcerpt())
                .map(dto -> {
                    dto.setUserBookId(userBook.getUserBookId());
                    return excerptService.createExcerpt(dto);
                })
                .orElse(null);
        // Review 생성 시 결정된 UserBook 사용
        Review review = Optional.ofNullable(requestDto.getReview())
                .map(dto -> {
                    dto.setUserBookId(userBook.getUserBookId());
                    return reviewService.createReview(dto);
                })
                .orElse(null);
        Archive archive = requestDto.toEntity(excerpt, review);
        archiveRepository.save(archive);
        return createArchiveResponseDto(archive, excerpt, review, userBook);
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
        UserBook userBook = userBookService.getUserBookFromExcerptOrReview(archive.getExcerpt(), archive.getReview());
        return createArchiveResponseDto(archive, archive.getExcerpt(), archive.getReview(), userBook);
    }

    public ArchiveResponseDto createArchiveResponseDto(Archive archive, Excerpt excerpt, Review review, UserBook userBook) {
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
