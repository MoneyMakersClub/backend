package com.mmc.bookduck.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserSetting", description = "사용자 환경 설정 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
public class UserSettingController {
}
