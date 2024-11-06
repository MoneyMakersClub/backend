package com.mmc.bookduck.domain.book.dto.response;

import java.util.List;

public record UserBookListResponseDto(
        int bookCount,
        List<UserBookResponseDto> bookList
) {
    public UserBookListResponseDto(List<UserBookResponseDto> bookList) {
        this(bookList != null ? bookList.size() : 0, bookList);
    }
}