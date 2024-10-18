package com.mmc.bookduck.domain.user.controller;

import com.mmc.bookduck.domain.user.dto.request.UserNicknameRequestDto;
import com.mmc.bookduck.domain.user.dto.request.UserSettingUpdateRequestDto;
import com.mmc.bookduck.domain.user.service.UserSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserSetting", description = "사용자 환경 설정 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
public class UserSettingController {
    private final UserSettingService userSettingService;

    @Operation(summary = "설정 정보 보기", description = "유저의 계정 정보, 사용 설정, 기록 폰트 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getUserSettings() {
        return ResponseEntity.ok().body(userSettingService.getUserSetting());
    }

    @Operation(summary = "닉네임 사용 가능 여부 확인", description = "닉네임 사용 가능 여부를 확인합니다.")
    @GetMapping("/nickname/check")
    public ResponseEntity<?> checkNicknameAvailability(@RequestBody @Valid UserNicknameRequestDto requestDto) {
        return ResponseEntity.ok().body(userSettingService.checkNicknameAvailability(requestDto));
    }

    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    @PatchMapping("/nickname")
    public ResponseEntity<?> updateNickname(@RequestBody @Valid UserNicknameRequestDto requestDto) {
        userSettingService.updateNickname(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "설정 옵션 변경", description = "사용 설정 및 기록 폰트 옵션을 변경합니다.")
    @PatchMapping("/options")
    public ResponseEntity<?> updateOptions(@RequestBody @Valid UserSettingUpdateRequestDto requestDto) {
        userSettingService.updateOptions(requestDto);
        return ResponseEntity.ok().build();
    }
}
