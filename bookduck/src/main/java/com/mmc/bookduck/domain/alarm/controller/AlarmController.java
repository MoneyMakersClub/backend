package com.mmc.bookduck.domain.alarm.controller;

import com.mmc.bookduck.domain.alarm.service.EmitterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Alarm", description = "Alarm 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {
    private final EmitterService emitterService;
    private final com.mmc.bookduck.domain.alarm.service.AlarmService alarmService;

    @Operation(summary = "알림 구독", description = "알림을 구독합니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // Content-Type 지정
    public ResponseEntity<SseEmitter> subscribe() {
        return ResponseEntity.ok(emitterService.subscribe());
    }

    @Operation(summary = "일반 알림 목록 조회 및 읽음 처리", description = "일반 알림 목록을 조회하고 읽음 처리합니다.")
    @PatchMapping("/common")
    public ResponseEntity<?> getRecentAlarms() {
        return ResponseEntity.ok(alarmService.getAndReadRecentAlarms());
    }

    @Operation(summary = "공지 목록 조회 및 읽음 처리", description = "공지 목록을 조회하고 읽음 처리합니다.")
    @PatchMapping("/announcements")
    public ResponseEntity<?> getRecentAnnouncements() {
        return ResponseEntity.ok(alarmService.getRecentAnnouncements());
    }
}