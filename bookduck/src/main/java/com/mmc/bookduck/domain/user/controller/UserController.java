package com.mmc.bookduck.domain.user.controller;

import com.mmc.bookduck.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 검색", description = "유저를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam("keyword") final String keyword,
                                           @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(userService.searchUsers(keyword, pageable));
    }
    
    @Operation(summary = "유저 정보 조회", description = "유저의 닉네임과 기록 수를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userService.getUserInfo(userId));
    }
}
