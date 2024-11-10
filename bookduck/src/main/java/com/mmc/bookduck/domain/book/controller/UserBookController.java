package com.mmc.bookduck.domain.book.controller;

import com.mmc.bookduck.domain.book.dto.request.CustomBookRequestDto;
import com.mmc.bookduck.domain.book.dto.request.RatingRequestDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoAdditionalResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.CustomBookResponseDto;
import com.mmc.bookduck.domain.book.dto.response.RatingResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookReviewExcerptResponseDto;
import com.mmc.bookduck.domain.book.service.UserBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Books", description = "Books 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class UserBookController {

    private final UserBookService userBookService;

    @Operation(summary = "서재에 책 추가", description = "사용자의 서재에 책을 추가합니다.")
    @PostMapping
    public ResponseEntity<UserBookResponseDto> addUserBook(@Valid @RequestBody UserBookRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userBookService.addUserBook(dto));
    }


    @Operation(summary = "서재에서 책 삭제", description = "사용자의 서재에서 책을 삭제합니다.")
    @DeleteMapping("/{userBookId}")
    public ResponseEntity<String> deleteUserBook(@PathVariable final Long userBookId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.deleteUserBook(userBookId));
    }


    @Operation(summary = "서재 책 상태 변경", description = "사용자의 서재의 책 상태를 변경합니다.")
    @PatchMapping("/{userBookId}")
    public ResponseEntity<UserBookResponseDto> updateUserBookStatus(@PathVariable final Long userBookId,
                                                                    @RequestParam(name = "status") final String status){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.updateUserBookStatus(userBookId, status));
    }


    @Operation(summary = "서재 책 목록 조회", description = "사용자의 서재 책 전체 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<UserBookListResponseDto> getAllUserBook(@RequestParam(name = "sort") final String sort){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getAllUserBook(sort));
    }

    @Operation(summary = "상태별 서재 책 목록 조회", description = "사용자의 서재 책 목록을 상태별로 조회합니다.")
    @GetMapping("/filter")
    public ResponseEntity<UserBookListResponseDto> getStatusUserBook(@RequestParam(name = "status") final List<String> statusList,
                                                                     @RequestParam(name = "sort") final String sort){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getStatusUserBook(statusList, sort));
    }


    @Operation(summary = "서재 책 상세-기본 정보 조회", description = "사용자의 서재 책의 기본 정보를 상세 조회합니다.(책 기본정보 + 현재 사용자의 별점&한줄평)")
    @GetMapping("/{userbookId}")
    public ResponseEntity<BookInfoBasicResponseDto> getUserBookInfoBasic(@PathVariable(name = "userbookId") final Long userbookId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getUserBookInfoBasic(userbookId));
    }


    @Operation(summary = "서재 책 상세-추가 정보 조회", description = "사용자의 서재 책의 추가 정보를 상세 조회합니다.(현재 책에 대한 다른 사용자들의 별점&한줄평 목록 3개)")
    @GetMapping("/{userbookId}/additional")
    public ResponseEntity<BookInfoAdditionalResponseDto> getUserBookInfoAdditional(@PathVariable(name = "userbookId") final Long userbookId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getUserBookInfoAdditional(userbookId));
    }


    @Operation(summary = "책 직접 등록", description = "사용자가 책을 직접 등록합니다.")
    @PostMapping(value = "/custom", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CustomBookResponseDto> createCustomBook(@Valid @ModelAttribute final CustomBookRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body((userBookService.createCustomBook(requestDto)));
    }


    //userBook의 기록과 발췌 통합 조회
    @Operation(summary = "서재책/Custom책 전체 기록 조회", description = "사용자의 서재책/Custom책의 전체 기록을 조회합니다.(감상평+발췌)")
    @GetMapping("/{userbookId}/reviewexcerpt")
    public ResponseEntity<UserBookReviewExcerptResponseDto> getAllUserBookReviewExcerpt(@PathVariable(name = "userbookId") final Long userbookId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getAllUserBookReviewExcerpt(userbookId));
    }


    //별점 등록
    @Operation(summary = "별점 등록(수정)", description = "서재 책의 별점을 등록(수정)합니다.")
    @PatchMapping("/{userbookId}/rating")
    public ResponseEntity<RatingResponseDto> ratingUserBook(@PathVariable(name = "userbookId") final Long userbookId,
                                                            @Valid @RequestBody final RatingRequestDto dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.ratingUserBook(userbookId, dto));
    }

    //별점 삭제
    @Operation(summary = "별점 삭제", description = "서재 책의 별점을 삭제합니다.")
    @DeleteMapping("/{userbookId}/rating")
    public ResponseEntity<RatingResponseDto> deleteRating(@PathVariable(name = "userbookId") final Long userbookId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.deleteRating(userbookId));
    }
}