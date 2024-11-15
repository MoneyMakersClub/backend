package com.mmc.bookduck.domain.user.controller;

import com.mmc.bookduck.domain.archive.dto.response.UserArchiveResponseDto;
import com.mmc.bookduck.domain.archive.entity.ArchiveType;
import com.mmc.bookduck.domain.archive.service.ArchiveService;
import com.mmc.bookduck.domain.item.service.UserItemService;
import com.mmc.bookduck.domain.user.service.*;
import com.mmc.bookduck.domain.userhome.service.UserReadingSpaceService;
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
    private final UserGrowthService userGrowthService;
    private final UserItemService userItemService;
    private final UserSearchService userSearchService;
    private final UserReadingReportService userReadingReportService;
    private final UserReadingSpaceService userReadingSpaceService;
    private final ArchiveService archiveService;

    @Operation(summary = "유저 검색", description = "유저를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam("keyword") final String keyword,
                                         @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(userSearchService.searchUsers(keyword, pageable));
    }

    @Operation(summary = "유저 정보 조회", description = "유저의 닉네임과 기록 수를 조회합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userGrowthService.getUserInfo(userId));
    }

    @Operation(summary = "유저 레벨, 경험치 조회", description = "유저의 레벨과 경험치를 조회합니다.")
    @GetMapping("/{userId}/growth")
    public ResponseEntity<?> getUserGrowthInfo(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userGrowthService.getUserLevelInfo(userId));
    }

    @Operation(summary = "유저 캐릭터가 착용한 아이템 조회", description = "유저 캐릭터가 착용한 아이템을 조회합니다.")
    @GetMapping("/{userId}/character")
    public ResponseEntity<?> getUserCharacterEquippedItems(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userItemService.getEquippedItemsOfUserByUserId(userId));
    }


    @Operation(summary = "유저 독서 리포트 조회 - 통계", description = "유저의 독서 리포트 중 통계를 조회합니다.")
    @GetMapping("/{userId}/statistics")
    public ResponseEntity<?> getUserStatistics(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userReadingReportService.getUserStatistics(userId));
    }

    @Operation(summary = "유저 독서 리포트 조회 - AI", description = "유저의 독서 리포트 중 AI부분을 조회합니다.")
    @GetMapping("/{userId}/ai")
    public ResponseEntity<?> getUserKeywordAnalysis(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userGrowthService.getUserKeywordAnalysis(userId));
    }

    @Operation(summary = "유저 리딩스페이스 조회", description = "유저 리딩스페이스를 조회합니다.")
    @GetMapping("/{userId}/readingspace")
    public ResponseEntity<?> getUserReadingSpace(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userReadingSpaceService.getUserReadingSpace(userId));
    }

    @Operation(summary = "유저 기록 아카이브 조회", description = "유저의 기록 아카이브를 조회합니다.")
    @GetMapping("{userId}/archives")
    public ResponseEntity<?> getUserArchive(@PathVariable("userId") final Long userId, @RequestParam("type") final ArchiveType archiveType,
                                            Pageable pageable){
        UserArchiveResponseDto responseDto = archiveService.getUserArchive(userId, archiveType, pageable);
        return ResponseEntity.ok(responseDto);
    }
}
