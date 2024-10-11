package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoBasicResponseDto {

    private Double ratingAverage;
    private ReadStatus readStatus;

    private String myOneLine;
    private Double myRating;

    private BookInfoDetailDto bookInfoDetailDto;

    @Builder
    public BookInfoBasicResponseDto(Double ratingAverage, String myOneLine, Double myRating, ReadStatus readStatus, BookInfoDetailDto bookInfoDetailDto) {
        this.ratingAverage = ratingAverage;
        this.myRating = myRating;
        this.myOneLine = myOneLine;
        this.readStatus = readStatus;
        this.bookInfoDetailDto = bookInfoDetailDto;
    }

}
