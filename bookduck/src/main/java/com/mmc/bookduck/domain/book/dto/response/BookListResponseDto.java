package com.mmc.bookduck.domain.book.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record BookListResponseDto<T>(
        Integer totalPages,
        Integer currentPage,
        Integer bookCount,
        List<T> bookList
) {
    public BookListResponseDto(List<T> bookList) {
        this(null, null, bookList != null ? bookList.size() : 0, bookList);
    }

    public BookListResponseDto(List<T> bookList, int totalPages, Long page) {
        this(totalPages, Math.toIntExact(page), bookList != null ? bookList.size() : 0, bookList);
    }
}