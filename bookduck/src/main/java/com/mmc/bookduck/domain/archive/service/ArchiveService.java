package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ArchiveUpdateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ReviewCreateRequestDto;
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

    // 생성
    public ArchiveResponseDto createArchive(ArchiveCreateRequestDto requestDto) {
        // UserBook 결정(createExcerpt,Review의 findById때문에)
        UserBook userBook = userBookService.getUserBookOrAdd(requestDto.getExcerpt(), requestDto.getReview(), requestDto);
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

    // 조회
    @Transactional(readOnly = true)
    public ArchiveResponseDto getArchive(Long id, ArchiveType archiveType) {
        Archive archive = findArchiveByType(id, archiveType);
        UserBook userBook = getUserBookFromExcerptOrReview(archive.getExcerpt().getExcerptId(), archive.getReview().getReviewId());
        return createArchiveResponseDto(archive, archive.getExcerpt(), archive.getReview(), userBook);
    }

    // 수정
    public ArchiveResponseDto updateArchive(Long id, ArchiveType archiveType, ArchiveUpdateRequestDto requestDto) {
        Archive archive = findArchiveByType(id, archiveType);
        UserBook userBook = getUserBookFromExcerptOrReview(archive.getExcerpt().getExcerptId(), archive.getReview().getReviewId());
        // 생성자 검증
        userBookService.validateUserBookOwner(userBook.getUserBookId());
        // 발췌 수정 혹은 생성
        Excerpt updatedExcerpt = Optional.ofNullable(requestDto.excerpt())
                .map(updateDto -> {
                    if (archive.getExcerpt() == null) {
                        ExcerptCreateRequestDto createRequestDto = ExcerptCreateRequestDto.builder()
                                .excerptContent(updateDto.excerptContent())
                                .pageNumber(updateDto.pageNumber())
                                .visibility(updateDto.excerptVisibility())
                                .userBookId(userBook.getUserBookId())
                                .build();
                        return excerptService.createExcerpt(createRequestDto);
                    } else {
                        return excerptService.updateExcerpt(archive.getExcerpt().getExcerptId(), updateDto);
                    }
                })
                .orElse(archive.getExcerpt());
        // 리뷰 수정 혹은 생성
        Review updatedReview = Optional.ofNullable(requestDto.review())
                .map(updateDto -> {
                    if (archive.getReview() == null) {
                        ReviewCreateRequestDto createRequestDto = ReviewCreateRequestDto.builder()
                                .reviewTitle(updateDto.reviewTitle())
                                .reviewContent(updateDto.reviewContent())
                                .color(updateDto.color())
                                .visibility(updateDto.reviewVisibility())
                                .userBookId(userBook.getUserBookId())
                                .build();
                        return reviewService.createReview(createRequestDto);
                    } else {
                        return reviewService.updateReview(archive.getReview().getReviewId(), updateDto);
                    }
                })
                .orElse(archive.getReview());
        archive.updateArchive(updatedExcerpt, updatedReview);
        archiveRepository.save(archive);
        return createArchiveResponseDto(archive, updatedExcerpt, updatedReview, userBook);
    }

    // 삭제
    public void deleteArchive(Long archiveId, Long reviewId, Long excerptId) {
        Archive archive = getArchiveById(archiveId);
        UserBook userBook = getUserBookFromExcerptOrReview(reviewId, excerptId);
        // 생성자 검증
        userBookService.validateUserBookOwner(userBook.getUserBookId());
        if (excerptId != null && archive.getExcerpt() != null) {
            if (!archive.getExcerpt().getExcerptId().equals(excerptId)) {
                throw new CustomException(ErrorCode.ARCHIVE_DOES_NOT_MATCH);
            }
            excerptService.deleteExcerpt(excerptId);
            archive.updateExcerpt(null);
        }
        if (reviewId != null && archive.getReview() != null) {
            if (!archive.getReview().getReviewId().equals(reviewId)) {
                throw new CustomException(ErrorCode.ARCHIVE_DOES_NOT_MATCH);
            }
            reviewService.deleteReview(reviewId);
            archive.updateReview(null);
        }
        // 만약 Excerpt와 Review가 모두 null이라면 Archive 엔티티 삭제
        if (archive.getExcerpt() == null && archive.getReview() == null) {
            archiveRepository.delete(archive);
        } else {
            // 변경된 내용 저장 (Archive가 삭제되지 않은 경우에만)
            archiveRepository.save(archive);
        }
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

    public Archive findArchiveByType(Long id, ArchiveType archiveType) {
        return switch (archiveType) {
            case EXCERPT -> archiveRepository.findByExcerpt_ExcerptId(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.EXCERPT_NOT_FOUND));
            case REVIEW -> archiveRepository.findByReview_ReviewId(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
            default -> throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        };
    }

    @Transactional(readOnly = true)
    public Archive getArchiveById(Long archiveId) {
        return archiveRepository.findById(archiveId)
                .orElseThrow(()-> new CustomException(ErrorCode.ARCHIVE_NOT_FOUND));
    }

    // UserBook 정보 불러오거나 없으면 생성하기
    @Transactional(readOnly = true)
    public UserBook getUserBookFromExcerptOrReview(Long excerptId, Long reviewId) {
        if (excerptId != null) {
            Excerpt excerpt = excerptService.getExcerptById(excerptId);
            return excerpt.getUserBook();
        } else if (reviewId != null) {
            Review review = reviewService.getReviewById(reviewId);
            return review.getUserBook();
        } else {
            throw new CustomException(ErrorCode.USERBOOK_NOT_FOUND);
        }
    }


}
