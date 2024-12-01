package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.common.ExcerptSearchUnitDto;
import com.mmc.bookduck.domain.archive.dto.common.ReviewSearchUnitDto;
import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ArchiveUpdateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ReviewCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.*;
import com.mmc.bookduck.domain.archive.entity.Archive;
import com.mmc.bookduck.domain.archive.entity.ArchiveType;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.archive.repository.ArchiveRepository;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.friend.repository.FriendRepository;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.common.PaginatedResponseDto;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mmc.bookduck.domain.archive.entity.ArchiveType.*;
import static com.mmc.bookduck.global.common.EscapeSpecialCharactersService.escapeSpecialCharacters;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {
    private final UserService userService;
    private final ExcerptService excerptService;
    private final ReviewService reviewService;
    private final UserBookService userBookService;
    private final ArchiveRepository archiveRepository;
    private final ExcerptRepository excerptRepository;
    private final ReviewRepository reviewRepository;
    private final FriendRepository friendRepository;

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
    public ArchiveResponseDto getArchive(Long archiveId) {
        Archive archive = getArchiveById(archiveId);
        UserBook userBook = getUserBookFromExcerptOrReview(archive);
        // currentUser와 creatorUser 사이의 관계에 따른 response 필터링
        Long currentUserId = userService.getCurrentUser().getUserId();
        Long creatorId = userBook.getUser().getUserId();
        boolean isCreator = creatorId.equals(currentUserId);
        boolean isFriend = friendRepository.findFriendBetweenUsers(currentUserId, creatorId).isPresent();
        Excerpt filteredExcerpt = isCreator || (isFriend && archive.getExcerpt().getVisibility() == Visibility.PUBLIC)
                ? archive.getExcerpt()
                : null;
        Review filteredReview = isCreator || (isFriend && archive.getReview().getVisibility() == Visibility.PUBLIC)
                ? archive.getReview()
                : null;
        return createArchiveResponseDto(archive, filteredExcerpt, filteredReview, userBook);
    }

    // 공유 링크를 통한 조회
    @Transactional(readOnly = true)
    public ArchiveResponseDto getSharedArchive(Long id, ArchiveType archiveType) {
        Archive archive = findArchiveByType(id, archiveType); // 기록 삭제되는 경우 에러 여기서 함께 처리됨

        // 공개 기록이 아니면 에러
        Visibility visibility = switch (archiveType) {
            case EXCERPT -> archive.getExcerpt().getVisibility();
            case REVIEW -> archive.getReview().getVisibility();
            default -> throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        };
        if (visibility == Visibility.PRIVATE) { throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);}

        UserBook userBook = getUserBookFromExcerptOrReview(archive);
        Excerpt publicExcerpt = archive.getExcerpt() != null && archive.getExcerpt().getVisibility() == Visibility.PUBLIC
                ? archive.getExcerpt()
                : null;
        Review publicReview = archive.getReview() != null && archive.getReview().getVisibility() == Visibility.PUBLIC
                ? archive.getReview()
                : null;
        return createArchiveResponseDto(archive, publicExcerpt, publicReview, userBook);
    }

    // 수정
    public ArchiveResponseDto updateArchive(Long archiveId, ArchiveUpdateRequestDto requestDto) {
        Archive archive = getArchiveById(archiveId);
        UserBook userBook = getUserBookFromExcerptOrReview(archive);
        // 생성자 검증
        userBookService.validateUserBookOwner(userBook);
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
        UserBook userBook = getUserBookFromExcerptOrReview(archive);
        // 생성자 검증
        userBookService.validateUserBookOwner(userBook);
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

    // 기록 아카이브 조회
    @Transactional(readOnly = true)
    public UserArchiveResponseDto getUserArchive(Long userId, ArchiveType archiveType, Pageable pageable){
        userService.getActiveUserByUserId(userId);
        Long currentUserId = userService.getCurrentUser().getUserId();
        // currentUserId가 userId와 다르면 친구인지 확인
        if (!userId.equals(currentUserId)) {
            Optional<Friend> friend = friendRepository.findFriendBetweenUsers(currentUserId, userId);
            if (!friend.isPresent()) {
                throw new CustomException(ErrorCode.FRIENDSHIP_REQUIRED);
            }
        }
        List<UserArchiveResponseDto.ArchiveWithType> archiveList = new ArrayList<>();
        // 발췌 조회
        if (archiveType == EXCERPT || archiveType == ArchiveType.ALL) {
            List<Excerpt> excerpts = excerptRepository.findByUserId(userId);
            for (Excerpt excerpt : excerpts) {
                if (!userId.equals(currentUserId) && excerpt.getVisibility() != Visibility.PUBLIC) {
                    continue;
                }
                Long archiveId = findArchiveByType(excerpt.getExcerptId(), EXCERPT).getArchiveId();
                String title = excerpt.getUserBook().getBookInfo().getTitle();
                String author = excerpt.getUserBook().getBookInfo().getAuthor();
                archiveList.add(new UserArchiveResponseDto.ArchiveWithType(EXCERPT, ExcerptResponseDto.from(excerpt), archiveId, title, author));
            }
        }
        // 리뷰 조회
        if (archiveType == ArchiveType.REVIEW || archiveType == ArchiveType.ALL) {
            List<Review> reviews = reviewRepository.findByUserId(userId);
            for (Review review : reviews) {
                if (!userId.equals(currentUserId) && review.getVisibility() != Visibility.PUBLIC) {
                    continue;
                }
                Long archiveId = findArchiveByType(review.getReviewId(), REVIEW).getArchiveId();
                String title = review.getUserBook().getBookInfo().getTitle();
                String author = review.getUserBook().getBookInfo().getAuthor();
                archiveList.add(new UserArchiveResponseDto.ArchiveWithType(REVIEW, ReviewResponseDto.from(review), archiveId,title, author));
            }
        }
        // 데이터 합친 후 최신순 정렬
        archiveList.sort((a1, a2) -> {
            LocalDateTime time1 = (a1.data() instanceof ExcerptResponseDto) ?
                    ((ExcerptResponseDto) a1.data()).createdTime() :
                    ((ReviewResponseDto) a1.data()).createdTime();
            LocalDateTime time2 = (a2.data() instanceof ExcerptResponseDto) ?
                    ((ExcerptResponseDto) a2.data()).createdTime() :
                    ((ReviewResponseDto) a2.data()).createdTime();
            return time2.compareTo(time1);
        });
        Page<UserArchiveResponseDto.ArchiveWithType> dtoPage = new PageImpl<>(archiveList, pageable, archiveList.size());
        return UserArchiveResponseDto.from(dtoPage);
    }

    // 나의 기록 검색
    @Transactional(readOnly = true)
    public ArchiveSearchListResponseDto searchArchives(String keyword, Pageable pageable, String orderBy) {
        Long userId = userService.getCurrentUser().getUserId();
        String escapedKeyword = escapeSpecialCharacters(keyword);

        Page<Object[]> rawResults = switch (orderBy.toLowerCase()) {
            case "accuracy" -> archiveRepository.searchByAccuracy(userId, escapedKeyword, pageable);
            case "latest" -> archiveRepository.searchByLatest(userId, escapedKeyword, pageable);
            default -> throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        };

        Page<ArchiveSearchListResponseDto.ResultWithType> resultWithTypePage = rawResults.map(row -> {
            String type = (String) row[0];
            Long id = (Long) row[1];
            String content = (String) row[2];
            String title = (String) row[3];
            Visibility visibility = Visibility.valueOf((String) row[4]);
            LocalDateTime createdTime = ((Timestamp) row[5]).toLocalDateTime();
            String bookTitle = (String) row[6];
            String bookAuthor = (String) row[7];

            if ("EXCERPT".equals(type)) {
                return new ArchiveSearchListResponseDto.ResultWithType(
                        EXCERPT,
                        new ExcerptSearchUnitDto(id, content, visibility, createdTime),
                        bookTitle,
                        bookAuthor
                );
            } else if ("REVIEW".equals(type)) {
                return new ArchiveSearchListResponseDto.ResultWithType(
                        REVIEW,
                        new ReviewSearchUnitDto(id, title, content, visibility, createdTime),
                        bookTitle,
                        bookAuthor
                );
            }
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        });

        PaginatedResponseDto<ArchiveSearchListResponseDto.ResultWithType> dtoPage =
                PaginatedResponseDto.from(resultWithTypePage);
        return ArchiveSearchListResponseDto.from(dtoPage);
    }


    public ArchiveResponseDto createArchiveResponseDto(Archive archive, Excerpt excerpt, Review review, UserBook userBook) {
        Long creatorUserId = userBook.getUser().getUserId();
        Long bookInfoId = userBook.getBookInfo().getBookInfoId();
        String title = userBook.getBookInfo().getTitle();
        String author = userBook.getBookInfo().getAuthor();
        String imgPath = userBook.getBookInfo().getImgPath();
        return ArchiveResponseDto.from(
                archive,
                creatorUserId,
                excerpt != null ? ExcerptResponseDto.from(excerpt) : null,
                review != null ? ReviewResponseDto.from(review) : null,
                bookInfoId,
                title,
                author,
                imgPath
        );
    }

    @Transactional(readOnly = true)
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

    // UserBook 정보 불러오기
    @Transactional(readOnly = true)
    public UserBook getUserBookFromExcerptOrReview(Archive archive) {
        Long excerptId = (archive.getExcerpt() != null) ? archive.getExcerpt().getExcerptId() : null;
        Long reviewId = (archive.getReview() != null) ? archive.getReview().getReviewId() : null;
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
