package com.mmc.bookduck.domain.badge.controller;

import com.mmc.bookduck.domain.badge.service.UserBadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserBadge", description = "UserBadge 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class UserBadgeController {
    private final UserBadgeService userBadgeService;

    @Operation(summary = "유저의 뱃지 목록 조회", description = "유저의 닉네임과 기록 수를 조회합니다.")
    @GetMapping("/users/{userId}/badges")
    public ResponseEntity<?> getUserBadges(@PathVariable final Long userId) {
        return ResponseEntity.ok().body(userBadgeService.getUserBadges(userId));
    }
}
