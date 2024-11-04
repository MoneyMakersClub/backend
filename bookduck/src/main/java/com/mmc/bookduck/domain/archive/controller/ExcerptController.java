package com.mmc.bookduck.domain.archive.controller;

import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.ExcerptResponseDto;
import com.mmc.bookduck.domain.archive.service.ExcerptService;
import com.mmc.bookduck.domain.archive.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excerpts")
@Tag(name = "Excerpt", description = "발췌 관련 API입니다.")
public class ExcerptController {

    private final OcrService ocrService;
    private final ExcerptService excerptService;

    @PostMapping("/ocr")
    @Operation(summary = "OCR을 통한 텍스트 추출", description = "이미지를 업로드하여 텍스트를 OCR로 추출합니다.")
    public ResponseEntity<String> uploadAndExtractText(@RequestParam("image") final MultipartFile image) throws IOException {
        String extractedText = ocrService.processOcr(image);
        return ResponseEntity.ok(extractedText);
    }

    @PostMapping
    @Operation(summary = "발췌 생성", description = "OCR로 인식된 텍스트 또는 사용자가 입력한 텍스트를 활용해 발췌를 생성합니다.")
    public ResponseEntity<?> createExcerpt(@Valid @RequestBody ExcerptCreateRequestDto requestDto) {
        ExcerptResponseDto responseDto = excerptService.createExcerpt(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
