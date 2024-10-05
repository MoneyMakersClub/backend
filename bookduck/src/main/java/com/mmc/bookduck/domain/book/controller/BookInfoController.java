package com.mmc.bookduck.domain.book.controller;

import com.mmc.bookduck.domain.book.dto.response.ApiBookBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookAdditionalListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.service.BookInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booksinfo")
public class BookInfoController {

    private final BookInfoService bookInfoService;

    // 도서목록 GET (API)
    @GetMapping("/search")
    public BookListResponseDto searchBookList(@RequestParam(name = "keyword") String keyword,
                                              @RequestParam Long page,
                                              @RequestParam Long size){

        return bookInfoService.searchBookList(keyword, page, size);
    }

//    // 도서목록 GET (직접등록)
//    @GetMapping("/search/custom")
//    public BookListResponseDto searchCustomBookList(@RequestParam(name = "keyword") String keyword,
//                                                    @RequestParam Long page,
//                                                    @RequestParam Long size){
//        return bookInfoService.searchCustomBookList(keyword, page, size);
//    }

    // API 도서 상세 정보 GET - 기본 정보
    @GetMapping("/external/{providerId}")
    public ApiBookBasicResponseDto getApiBookBasic(@PathVariable(name = "providerId") String providerId){
        return bookInfoService.getApiBookBasic(providerId);
    }

//    // API 도서 상세 정보 GET - 상세 정보
//    @GetMapping("/external/{providerId}/additional")
//    public BookAdditionalListResponseDto getApiBookAdditional(@PathVariable(name = "providerId") String providerId){
//        return bookInfoService.getApiBoolAdditional(providerId);
//    }

    // 커스텀 도서 상세 정보 GET - 기본 정보 + 사용자의 한줄평,별점

    // 도서 직접 등록

}
