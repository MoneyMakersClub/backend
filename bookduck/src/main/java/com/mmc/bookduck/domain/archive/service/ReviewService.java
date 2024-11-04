package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ReviewCreateRequestDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final UserBookService userBookService;

    public Review createReview(ReviewCreateRequestDto requestDto, LocalDateTime createdTime){
        User user = userService.getCurrentUser();
        UserBook userBook = userBookService.findUserBookById(requestDto.userBookId());
        String color = requestDto.color() != null ? requestDto.color() : "#FFFFFF";
        Review review = requestDto.toEntity(user, userBook, color);
        review.setCreatedTime(createdTime);
        return reviewRepository.save(review);
    }
}
