package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ReviewCreateRequestDto;
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
        UserBook userBook = userBookService.findUserBookById(requestDto.userBookId());
        String color = requestDto.color() != null ? requestDto.color() : "#FFFFFF";
        Visibility visibility = requestDto.visibility() != null ? requestDto.visibility() : Visibility.PUBLIC;
        Review review = requestDto.toEntity(user, userBook, color, visibility);
        return reviewRepository.save(review);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
