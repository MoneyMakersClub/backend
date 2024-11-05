package com.mmc.bookduck.domain.book.dto.response;

import java.util.List;

public record BookListResponseDto<T>(
        int bookCount,
        List<T> bookList
) {
    public BookListResponseDto(List<T> bookList) {
        this(bookList != null ? bookList.size() : 0, bookList);
    }
}