package com.mmc.bookduck.domain.book.controller;

import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class UserBookController {

    private final UserBookService userBookService;

    // 서재에 책 추가
    @PostMapping
    public UserBookResponseDto addUserBook(@RequestBody UserBookRequestDto dto,
                                           @RequestParam(name="status")String status){
        return userBookService.addUserBook(dto, status);
    }

    // 서재에서 책 삭제
    @DeleteMapping("/{userBookId}")
    public String deleteUserBook(@PathVariable Long userBookId){
        return userBookService.deleteUserBook(userBookId);
    }

    // 서재 책 상태 변경
    @PatchMapping("/{userBookId}")
    public UserBookResponseDto updateUserBookStatus(@PathVariable Long userBookId,
                                                    @RequestParam(name = "status")String status){
        return userBookService.updateUserBookStatus(userBookId, status);
    }
}
