package com.mmc.bookduck.domain.book.dto.request;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record CustomBookRequestDto(@NotBlank String title,
                                   @NotBlank String author,
                                   Long pageCount,
                                   String publisher,
                                   MultipartFile coverImage) {

    public BookInfo toEntity(String imgPath, Genre genre, Long userId){
        return BookInfo.builder()
                .title(this.title)
                .author(this.author)
                .pageCount(this.pageCount)
                .publisher(this.publisher)
                .imgPath(imgPath)
                .genre(genre)
                .createdUserId(userId)
                .build();
    }
}

