package com.mmc.bookduck.domain.book.controller;

import com.mmc.bookduck.domain.book.dto.request.CustomBookUpdateDto;
import com.mmc.bookduck.domain.book.dto.response.CustomBookResponseDto;
import com.mmc.bookduck.domain.book.dto.common.CustomBookUnitDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoAdditionalResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookUnitResponseDto;
import com.mmc.bookduck.domain.book.service.BookInfoService;
import com.mmc.bookduck.domain.oneline.dto.response.OneLineRatingListResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@Tag(name = "BookInfo", description = "BookInfo 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookinfo")
public class BookInfoController {
    private final BookInfoService bookInfoService;

    @Operation(summary = "API 도서 목록 검색", description = "구글 API에서 특정 키워드에 해당하는 도서 목록을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<BookListResponseDto<BookUnitResponseDto>> searchBookList(@RequestParam(name = "keyword") final String keyword,
                                                                                   @RequestParam final Long page,
                                                                                   @RequestParam final Long size){

        return ResponseEntity.ok(bookInfoService.searchBookList(keyword, page, size));
    }

    @Operation(summary = "bookInfoId로 API 도서 상세-기본 정보 조회", description = "bookInfoId로 구글 API 도서의 기본 정보를 상세 조회합니다.(책 정보 + 현재 사용자의 별점&한줄평)")
    @GetMapping("/{bookinfoId}")
    public ResponseEntity<BookInfoBasicResponseDto> getApiBookBasicByBookInfoId(@PathVariable(name = "bookinfoId") final Long bookinfoId){
        return ResponseEntity.ok(bookInfoService.getApiBookBasicByBookInfoId(bookinfoId));
    }


    @Operation(summary = "providerId로 API 도서 상세-기본 정보 조회", description = "providerId로 구글 API 도서의 기본 정보를 상세 조회합니다.(책 정보 + 현재 사용자의 별점&한줄평)")
    @GetMapping("/external/{providerId}")
    public ResponseEntity<BookInfoBasicResponseDto> getApiBookBasicByProviderId(@PathVariable(name = "providerId") final String providerId){
        return ResponseEntity.ok(bookInfoService.getApiBookBasicByProviderId(providerId));
    }


    @Operation(summary = "API 도서 상세-추가 정보 조회", description = "구글 API 도서의 추가 정보를 상세 조회합니다.(현재 책에 대한 다른 사용자들의 별점&한줄평 목록 3개)")
    @GetMapping("/external/{providerId}/additional")
    public ResponseEntity<BookInfoAdditionalResponseDto> getApiBookAdditional(@PathVariable(name = "providerId") String providerId){
        return ResponseEntity.ok(bookInfoService.getApiBookAdditional(providerId));
    }


    @Operation(summary = "사용자가 직접 추가한 도서 목록 검색", description = "현재 사용자가 직접 추가한 도서 중에서 특정 키워드에 해당하는 도서 목록을 검색합니다.")
    @GetMapping("/search/custom")
    public ResponseEntity<BookListResponseDto<CustomBookUnitDto>> searchCustomBookList(@RequestParam(name = "keyword") final String keyword,
                                                                                       @RequestParam final Long page,
                                                                                       @RequestParam final Long size){
        return ResponseEntity.ok(bookInfoService.searchCustomBookList(keyword, page, size));
    }


    @Operation(summary = "사용자가 직접 추가한 도서 상세-기본 정보 조회", description = "사용자가 직접 추가한 도서의 기본 정보를 상세 조회합니다.(책 정보 + 현재 사용자의 별점&한줄평)")
    @GetMapping("/custom/{bookinfoId}")
    public ResponseEntity<CustomBookResponseDto> getCustomBookBasic(@PathVariable(name = "bookinfoId") final Long bookInfoId){
        return ResponseEntity.ok(bookInfoService.getCustomBookBasic(bookInfoId));
    }

    @Operation(summary = "사용자가 직접 추가한 도서 정보 수정", description = "사용자가 직접 등록한 도서의 정보를 수정합니다.")
    @PatchMapping("/custom/{bookinfoId}")
    public ResponseEntity<CustomBookResponseDto> updateCustomBookInfo(@PathVariable(name = "bookinfoId") final Long bookInfoId,
                                                                      @ModelAttribute final CustomBookUpdateDto dto){
        return ResponseEntity.ok(bookInfoService.updateCustomBookInfo(bookInfoId, dto));
    }

    @GetMapping("/{bookinfoId}/onelines")
    @Operation(summary = "도서의 한줄평/별점 목록 조회", description = "도서의 한줄평&별점 목록을 조회합니다.")
    public ResponseEntity<?> getOneLineList(@PathVariable("bookinfoId") Long bookInfoId, @RequestParam(defaultValue = "likes") String sortOption, Pageable pageable){
        OneLineRatingListResponseDto responseDto = bookInfoService.getOneLineList(bookInfoId, sortOption, pageable);
        return ResponseEntity.ok().build();
    }
}
