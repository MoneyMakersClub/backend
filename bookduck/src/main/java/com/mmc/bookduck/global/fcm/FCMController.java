package com.mmc.bookduck.global.fcm;

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

    @PostMapping("/{userId}/token")
    public ResponseEntity<String> getFcmToken(@PathVariable Long userId,
                                             @Valid @RequestBody PostTokenReq postTokenReq) {
        return ResponseEntity.ok().body(fcmService.getFcmToken(userId, postTokenReq.getFcmToken()));
    }
}
