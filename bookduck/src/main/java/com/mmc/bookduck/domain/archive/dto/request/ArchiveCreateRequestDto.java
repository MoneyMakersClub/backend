package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.archive.entity.Archive;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.dto.request.AddUserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.request.AddCustomBookRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArchiveCreateRequestDto {

    private ExcerptCreateRequestDto excerpt;
    private ReviewCreateRequestDto review;
    private String providerId;
    private AddUserBookRequestDto userBook;
    private AddCustomBookRequestDto customBook;

    public ArchiveCreateRequestDto(ExcerptCreateRequestDto excerpt, ReviewCreateRequestDto review, String providerId, AddUserBookRequestDto userBook, AddCustomBookRequestDto customBook) {
        this.excerpt = excerpt;
        this.review = review;
        this.providerId = providerId;
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

