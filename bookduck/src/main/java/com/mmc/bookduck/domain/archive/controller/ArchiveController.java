package com.mmc.bookduck.domain.archive.controller;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ArchiveUpdateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.ArchiveResponseDto;
import com.mmc.bookduck.domain.archive.entity.ArchiveType;
import com.mmc.bookduck.domain.archive.service.ArchiveService;
import com.mmc.bookduck.domain.archive.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/archives")
@Tag(name = "Archive", description = "발췌 및 감상평 기록하기 관련 API입니다.")
public class ArchiveController {
    private final OcrService ocrService;
    private final ArchiveService archiveService;

    @PostMapping(value = "/excerpts/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "OCR을 통한 텍스트 추출", description = "이미지를 업로드하여 텍스트를 OCR로 추출합니다.")
    public ResponseEntity<?> uploadAndExtractText(@RequestParam("image") final MultipartFile image) throws IOException {
        String extractedText = ocrService.processOcr(image);
        return ResponseEntity.ok(extractedText);
    }

    @PostMapping
    @Operation(summary = "발췌 및 감상평 생성", description = "발췌와 감상평을 동시에 또는 선택적으로 생성합니다.")
    public ResponseEntity<?> createArchive(@Valid @RequestBody ArchiveCreateRequestDto requestDto) {
        ArchiveResponseDto responseDto = archiveService.createArchive(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "발췌 및 감상평 통합 조회", description = "발췌와 감상평을 조회합니다.")
    public ResponseEntity<?> getArchive(@PathVariable("id") final Long id, @RequestParam("type") final ArchiveType archiveType) {
        ArchiveResponseDto responseDto = archiveService.getArchive(id, archiveType);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "발췌 및 감상평 통합 수정", description = "발췌와 감상평을 수정합니다.(없던 종류의 독서기록을 남기는 것 역시 가능함)")
    public ResponseEntity<?> updateArchive(@PathVariable("id") final Long id, @RequestParam("type") final ArchiveType archiveType,
                                           @Valid @RequestBody ArchiveUpdateRequestDto requestDto) {
        ArchiveResponseDto responseDto = archiveService.updateArchive(id, archiveType, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{archiveId}")
    @Operation(summary = "발췌 및 감상평 통합 삭제", description = "발췌와 감상평을 동시에 또는 선택적으로 삭제합니다.")
    public ResponseEntity<?> deleteArchive(@PathVariable("archiveId") final Long archiveId,
                                           @RequestParam(name = "excerptId", required = false) Long excerptId, @RequestParam(name = "reviewId", required = false) Long reviewId){
        archiveService.deleteArchive(archiveId, reviewId, excerptId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "나의 기록 검색", description = "나의 기록(발췌, 감상평)을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam("keyword") final String keyword,
                                         @PageableDefault(size = 20) final Pageable pageable) {
        archiveService.searchArchives(keyword, pageable);
        return ResponseEntity.ok().body();
    }
}