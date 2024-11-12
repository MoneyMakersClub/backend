package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.entity.UserBook;

public record RatingResponseDto(Long userbookId,
                               double rating){
    public static RatingResponseDto from(UserBook userBook){
        return new RatingResponseDto(
                userBook.getUserBookId(),
                userBook.getRating()
        );
    }
}
