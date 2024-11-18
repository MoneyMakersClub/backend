package com.mmc.bookduck.domain.export.controller;

import com.mmc.bookduck.domain.export.dto.ExportCharResponseDto;
import com.mmc.bookduck.domain.export.dto.ExportStatsResponseDto;
import com.mmc.bookduck.domain.export.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Export", description = "Export 관련 API입니다.")
@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {
    private final ExportService exportService;

    @GetMapping("/character")
    @Operation(summary = "캐릭터 내보내기 정보 제공", description = "캐릭터 내보내기 할 정보를 조회합니다.")
    public ResponseEntity<?> getCharacterExportInfo() {
        ExportCharResponseDto responseDto = exportService.getCharExportInfo();
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/statistics")
    @Operation(summary = "통계 요약 내보내기 정보 제공", description = "통계 요약 내보내기 할 정보를 조회합니다.")
    public ResponseEntity<?> getStatisticsExportInfo() {
        ExportStatsResponseDto responseDto = exportService.getStatsExportInfo();
        return ResponseEntity.ok(responseDto);
    }
}


