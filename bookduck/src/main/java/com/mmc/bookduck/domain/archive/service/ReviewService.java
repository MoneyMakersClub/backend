package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ExcerptUpdateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ReviewCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ReviewUpdateRequestDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final UserBookService userBookService;

    // 생성
    public Review createReview(ReviewCreateRequestDto requestDto){
        User user = userService.getCurrentUser();
        UserBook userBook = userBookService.getUserBookById(requestDto.getUserBookId());
        boolean isSystemGenerated = false;
        // 제목 생성 로직
        String reviewTitle = requestDto.getReviewTitle();
        if (reviewTitle == null || reviewTitle.isBlank()) {
            String dateTitle = formatDateTitle(userBook.getCreatedTime());
            reviewTitle = generateUniqueTitle(user, dateTitle);
            isSystemGenerated = true;
        }
        Review review = requestDto.toEntity(user, userBook);
        review.setReviewTitle(reviewTitle);
        review.setIsSystemGenerated(isSystemGenerated);
        return reviewRepository.save(review);
    }

    // 수정
    public Review updateReview(Long reviewId, ReviewUpdateRequestDto requestDto) {
        // 생성자 검증 archiveService.updateArchive에서 하고 있으므로 생략
        Review review = getReviewById(reviewId);
        // 제목 업데이트 시 isSystemGenerated를 false로 변경
        String newTitle = requestDto.reviewTitle();
        if (newTitle != null && !newTitle.equals(review.getReviewTitle())) {
            review.setReviewTitle(newTitle);
            review.setIsSystemGenerated(false);
        }
        review.updateReview(requestDto.reviewTitle(), requestDto.reviewContent(), requestDto.color(), requestDto.reviewVisibility());
        return review;
    }

    // 삭제
    public void deleteReview(Long reviewId) {
        // 생성자 검증 archiveService.deleteArchive에서 하고 있으므로 생략
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    public String formatDateTitle(LocalDateTime createdTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일의 기록");
        return createdTime.format(formatter);
    }

    private String generateUniqueTitle(User user, String dateTitle) {
        List<Review> existingReviews = reviewRepository.findByUserAndReviewTitleStartingWith(user, dateTitle);
        if (existingReviews.isEmpty()) {
            return dateTitle;
        }
        int maxSuffix = 0; // 숫자 접미사 설정
        for (Review review : existingReviews) {
            String title = review.getReviewTitle();
            // 'yyyy년 mm월 dd의 기록' 제목이 이미 존재하는 경우
            if (title.equals(dateTitle)) {
                if (maxSuffix < 1) { maxSuffix = 1; }
            }
            // 시스템이 생성한 접미사 제목이 있는 경우
            else if (title.matches(Pattern.quote(dateTitle) + "\\(\\d+\\)$")) {
                String suffixStr = title.substring(title.lastIndexOf("(") + 1, title.lastIndexOf(")"));
                try {
                    int suffix = Integer.parseInt(suffixStr); // 접미사 추출
                    if (suffix > maxSuffix) { maxSuffix = suffix; }
                } catch (NumberFormatException e) {
                    // 접미사가 숫자가 아닌 경우 무시
                }
            }
        }
        return maxSuffix == 0 ? dateTitle : dateTitle + "(" + (maxSuffix + 1) + ")";
    }
}
