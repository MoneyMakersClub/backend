package com.mmc.bookduck.domain.book.controller;

import com.mmc.bookduck.domain.book.dto.common.CustomBookUnitResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoAdditionalResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.service.BookInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookinfo")
public class BookInfoController {

    private final BookInfoService bookInfoService;

    // 도서목록 GET(API)
    @GetMapping("/search")
    public ResponseEntity<BookListResponseDto> searchBookList(@RequestParam(name = "keyword") final String keyword,
                                                             @RequestParam final Long page,
                                                             @RequestParam final Long size){

        return ResponseEntity.ok(bookInfoService.searchBookList(keyword, page, size));
    }

    // API 도서 상세 정보 GET - 기본 정보
    @GetMapping("/external/{providerId}")
    public ResponseEntity<BookInfoBasicResponseDto> getOneBookBasic(@PathVariable(name = "providerId") final String providerId){
        return ResponseEntity.ok(bookInfoService.getOneBookBasic(providerId));
    }

    // API 도서 상세 조회 - 추가 정보
    @GetMapping("/external/{providerId}/additional")
    public ResponseEntity<BookInfoAdditionalResponseDto> getApiBookAdditional(@PathVariable(name = "providerId") String providerId){
        return ResponseEntity.ok(bookInfoService.getApiBookAdditional(providerId));
    }

    // 도서목록 GET(CUSTOM)
    @GetMapping("/search/custom")
    public ResponseEntity<BookListResponseDto<CustomBookUnitResponseDto>> searchCustomBookList(@RequestParam(name = "keyword") final String keyword, @RequestParam final Long page,
                                                                                               @RequestParam final Long size){
        return ResponseEntity.ok(bookInfoService.searchCustomBookList(keyword, page, size));
    }

    // 커스텀 도서 상세 조회 - 기본 정보 + 사용자의 한줄평,별점
    @GetMapping("/custom/{bookinfoId}/additional")
    public ResponseEntity<BookInfoBasicResponseDto> getApiBookAdditional(@PathVariable(name = "bookinfoId") final Long bookInfoId){
        return ResponseEntity.ok(bookInfoService.getCustomBookBasic(bookInfoId));
    }
}
