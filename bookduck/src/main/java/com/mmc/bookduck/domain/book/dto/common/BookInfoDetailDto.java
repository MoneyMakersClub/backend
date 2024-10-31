package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.UserBook;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoDetailDto {
    private String publisher;
    private String publishedDate;
    private String description;
    private Long pageCount;
    private List<String> category;
    private Long genreId;
    private String koreanGenreName;
    private String language;

    @Builder
    public BookInfoDetailDto(String publisher, String publishedDate, String description, Long pageCount,
                             List<String> category,Long genreId, String koreanGenreName, String language){
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.description = description;
        this.pageCount = pageCount;
        this.category = category;
        this.genreId = genreId;
        this.koreanGenreName = koreanGenreName;
        this.language = language;
    }


    //새로추가
    public static BookInfoDetailDto from(UserBook userBook, String koreanGenreName) {
        return BookInfoDetailDto.builder()
                .publisher(userBook.getBookInfo().getPublisher())
                .publishedDate(userBook.getBookInfo().getPublishDate())
                .description(userBook.getBookInfo().getDescription())
                .pageCount(userBook.getBookInfo().getPageCount())
                .genreId(userBook.getBookInfo().getGenre().getGenreId())
                .pageCount(userBook.getBookInfo().getPageCount())
                .koreanGenreName(koreanGenreName)
                .language(userBook.getBookInfo().getLanguage())
                .build();
    }
}
