package com.mmc.bookduck.domain.book.controller;

import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.service.BookInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booksinfo")
public class BookInfoController {

    private final BookInfoService bookInfoService;

    // 도서검색 GET (일단 API)
    @GetMapping("/search")
    public BookListResponseDto searchBookList(@RequestParam String keyword,
                                              @RequestParam Long page,
                                              @RequestParam Long size){
        return bookInfoService.searchBookList(keyword, page, size);
    }

    // 도서 상세 정보 GET - 기본 정보

    // 도서 상세 정보 GET - 상세 정보

    // 도서 직접 등록

}
