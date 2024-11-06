package com.mmc.bookduck.domain.book.dto.response;

import java.util.List;

public record BookListResponseDto(
        int bookCount,
        List<BookUnitResponseDto> bookList
) {
    public BookListResponseDto(List<BookUnitResponseDto> bookList) {
        this(bookList != null ? bookList.size() : 0, bookList);
    }
}