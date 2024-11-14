package com.mmc.bookduck.global.fcm;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FCMTokenRequestDto {
    @NotBlank(message="토큰을 입력하세요.")
    String fcmToken;
}