package com.mmc.bookduck.global.fcm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "FCM", description = "FCM 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FCMController {
    private final FCMService fcmService;

    @Operation(summary = "FCM 토큰 저장", description = "유저의 FCM 토큰을 저장합니다.")
    @PostMapping("/{userId}/token")
    public ResponseEntity<String> setFcmToken(@PathVariable final Long userId,
                                             @RequestBody @Valid FCMTokenRequestDto requestDto) {
        fcmService.setFcmToken(userId, requestDto.getFcmToken());
        return ResponseEntity.ok().build();
    }
}
