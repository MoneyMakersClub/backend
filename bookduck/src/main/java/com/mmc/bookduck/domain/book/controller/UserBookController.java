package com.mmc.bookduck.domain.book.controller;

import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoAdditionalResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookResponseDto;
import com.mmc.bookduck.domain.book.service.UserBookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class UserBookController {

    private final UserBookService userBookService;

    @PostMapping
    public ResponseEntity<UserBookResponseDto> addUserBook(@RequestBody UserBookRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userBookService.addUserBook(dto));
    }

    // 서재에서 책 삭제
    @DeleteMapping("/{userBookId}")
    public ResponseEntity<String> deleteUserBook(@PathVariable final Long userBookId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.deleteUserBook(userBookId));
    }

    // 서재 책 상태 변경
    @PatchMapping("/{userBookId}")
    public ResponseEntity<UserBookResponseDto> updateUserBookStatus(@PathVariable final Long userBookId,
                                                                    @RequestParam(name = "status") final String status){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.updateUserBookStatus(userBookId, status));
    }

    // 서재 전체 조회
    @GetMapping("/list")
    public ResponseEntity<UserBookListResponseDto> getAllUserBook(@RequestParam(name = "sort") final String sort){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getAllUserBook(sort));
    }

    // 서재 책 상태별 조회
    @GetMapping("/filter")
    public ResponseEntity<UserBookListResponseDto> getStatusUserBook(@RequestParam(name = "status") final List<String> statusList,
                                                                     @RequestParam(name = "sort") final String sort){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getStatusUserBook(statusList, sort));
    }

    // 서재 책 상세 조회 - 기본 정보
    @GetMapping("/{userbookId}")
    public ResponseEntity<BookInfoBasicResponseDto> getUserBookInfoBasic(@PathVariable(name = "userbookId") final Long userbookId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getUserBookInfoBasic(userbookId));
    }

    // 서재 책 상세 조회 - 추가 정보
    @GetMapping("/{userbookId}/additional")
    public ResponseEntity<BookInfoAdditionalResponseDto> getUserBookInfoAdditional(@PathVariable(name = "userbookId") final Long userbookId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(userBookService.getUserBookInfoAdditional(userbookId));
    }
}