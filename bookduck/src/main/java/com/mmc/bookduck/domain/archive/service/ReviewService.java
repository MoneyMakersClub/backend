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

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final UserBookService userBookService;

    public Review createReview(ReviewCreateRequestDto requestDto){
        User user = userService.getCurrentUser();
        UserBook userBook = userBookService.getUserBookById(requestDto.getUserBookId());
        String color = requestDto.getColor() != null ? requestDto.getColor() : "#FFFFFF";
        Visibility visibility = requestDto.getVisibility() != null ? requestDto.getVisibility() : Visibility.PUBLIC;
        Review review = requestDto.toEntity(user, userBook, color, visibility);
        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, ReviewUpdateRequestDto requestDto) {
        // 생성자 검증 archiveService.updateArchive에서 하고 있으므로 생략
        Review review = getReviewById(reviewId);
        review.updateReview(requestDto.reviewTitle(), requestDto.reviewContent(), requestDto.color(), requestDto.reviewVisibility());
        return review;
    }

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
}
