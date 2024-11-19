package com.mmc.bookduck.domain.book.dto.request;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record CustomBookRequestDto(@NotBlank(message = "title은 필수입니다. title은 공백일 수 없습니다.") String title,
                                   @NotBlank(message = "author은 필수입니다. author은 공백일 수 없습니다.") String author,
                                   MultipartFile coverImage) {

    public BookInfo toEntity(String imgPath, Genre genre, Long userId){
        return BookInfo.builder()
                .title(this.title)
                .author(this.author)
                .imgPath(imgPath)
                .genre(genre)
                .createdUserId(userId)
                .build();
    }
}

