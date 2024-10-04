package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookInfoUnitDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookListResponseDto {
    private List<BookInfoUnitDto> bookList;

    @Builder
    public BookListResponseDto(List<BookInfoUnitDto> bookList){
        this.bookList = bookList;
    }
}
