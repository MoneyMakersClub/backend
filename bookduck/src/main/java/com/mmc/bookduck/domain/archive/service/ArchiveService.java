package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.ArchiveResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ExcerptResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ReviewResponseDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveService {
    private final ExcerptService excerptService;
    private final ReviewService reviewService;
    private final UserService userService;

    public ArchiveResponseDto createArchive(ArchiveCreateRequestDto requestDto) {
        LocalDateTime createdTime = LocalDateTime.now(); // 각 서비스에서 생성하므로 createTime이 미세하게 다를 것 방지
        Excerpt savedExcerpt = null;
        Review savedReview = null;
        if (requestDto.excerpt() != null) {
            savedExcerpt = excerptService.createExcerpt(requestDto.excerpt(), createdTime);
        }
        if (requestDto.review() != null) {
            savedReview = reviewService.createReview(requestDto.review(), createdTime);
        }
        return toArchiveResponseDto(savedExcerpt, savedReview);
    }

    public ArchiveResponseDto toArchiveResponseDto(Excerpt excerpt, Review review){
        ExcerptResponseDto excerptDto = excerpt != null ? ExcerptResponseDto.from(excerpt) : null;
        ReviewResponseDto reviewDto = review != null ? ReviewResponseDto.from(review) : null;
        String title = null;
        String author = null;
        if (excerpt != null) {
            BookInfo bookInfo = excerpt.getUserBook().getBookInfo();
            title = bookInfo.getTitle();
            author = bookInfo.getAuthor();
        } else if (review != null) {
            BookInfo bookInfo = review.getUserBook().getBookInfo();
            title = bookInfo.getTitle();
            author = bookInfo.getAuthor();
        }
        return new ArchiveResponseDto(excerptDto, reviewDto, title, author);
    }
}
