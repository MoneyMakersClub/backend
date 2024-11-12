package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.archive.entity.Archive;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.dto.request.CustomBookRequestDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArchiveCreateRequestDto {

    private ExcerptCreateRequestDto excerpt;
    private ReviewCreateRequestDto review;
    private UserBookRequestDto userBook;
    private CustomBookRequestDto customBook;

    public ArchiveCreateRequestDto(ExcerptCreateRequestDto excerpt, ReviewCreateRequestDto review, UserBookRequestDto userBook, CustomBookRequestDto customBook) {
        this.excerpt = excerpt;
        this.review = review;
        this.userBook = userBook;
        this.customBook = customBook;
    }

    public Archive toEntity(Excerpt excerpt, Review review) {
        return Archive.builder()
                .excerpt(excerpt)
                .review(review)
                .build();
    }

    public void setExcerpt(ExcerptCreateRequestDto excerpt) {
        this.excerpt = excerpt;
    }

    public void setReview(ReviewCreateRequestDto review) {
        this.review = review;
    }
}

