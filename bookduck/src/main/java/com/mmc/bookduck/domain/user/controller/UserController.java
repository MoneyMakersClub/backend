package com.mmc.bookduck.domain.user.controller;

import com.mmc.bookduck.domain.user.service.UserGrowthService;
import com.mmc.bookduck.domain.user.service.UserReadingReportService;
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
    private final UserGrowthService userGrowthService;
    private final UserReadingReportService userReadingReportService;

    @Operation(summary = "유저 검색", description = "유저를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam("keyword") final String keyword,
                                           @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(userService.searchUsers(keyword, pageable));
    }

    @Operation(summary = "유저 정보 조회", description = "유저의 닉네임과 기록 수를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userGrowthService.getUserInfo(userId));
    }

    @Operation(summary = "유저 레벨, 레벨업 미션 조회", description = "유저의 레벨과 레벨업 미션들을 조회합니다.")
    @GetMapping("/{userId}/growth")
    public ResponseEntity<?> getUserGrowthInfo(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userGrowthService.getUserGrowthInfo(userId));
    }

    @Operation(summary = "유저 독서 리포트 조회 - 통계", description = "유저의 독서 리포트 중 통계를 조회합니다.")
    @GetMapping("/{userId}/statistics")
    public ResponseEntity<?> getUserStatistics(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userReadingReportService.getUserStatistics(userId));
    }

//    @Operation(summary = "유저 독서 리포트 조회 - AI", description = "유저의 독서 리포트 중 AI부분을 조회합니다.")
//    @GetMapping("/{userId}/ai")
//    public ResponseEntity<?> getUserKeywordAnalysis(@PathVariable final Long userId) {
//        return ResponseEntity.ok().body(userGrowthService.getUserKeywordAnalysis(userId));
//    }
}
