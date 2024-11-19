package com.mmc.bookduck.domain.export.controller;

import com.mmc.bookduck.domain.archive.dto.response.ExcerptExportResponseDto;
import com.mmc.bookduck.domain.archive.dto.response.ReviewExportResponseDto;
import com.mmc.bookduck.domain.export.dto.ExportCharResponseDto;
import com.mmc.bookduck.domain.export.dto.ExportStatsResponseDto;
import com.mmc.bookduck.domain.export.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/excerptcard/{excerptId}")
    @Operation(summary = "발췌 카드 공유하기 정보 제공", description = "공유할 발췌 카드 이미지의 정보를 조회합니다.")
    public ResponseEntity<?> getExcerptCardInfo(@PathVariable("excerptId") final Long excerptId) {
        ExcerptExportResponseDto responseDto = exportService.getExcerptCardInfo(excerptId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/reviewcard/{reviewId}")
    @Operation(summary = "감상평 카드 공유하기 정보 제공", description = "공유할 감상평 카드 이미지의 정보를 조회합니다.")
    public ResponseEntity<?> getReviewCardInfo(@PathVariable("reviewId") final Long reviewId) {
        ReviewExportResponseDto responseDto = exportService.getReviewCardInfo(reviewId);
        return ResponseEntity.ok(responseDto);
    }
}


