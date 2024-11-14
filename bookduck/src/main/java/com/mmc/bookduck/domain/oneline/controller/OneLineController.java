package com.mmc.bookduck.domain.oneline.controller;

import com.mmc.bookduck.domain.oneline.dto.request.OneLineCreateRequestDto;
import com.mmc.bookduck.domain.oneline.dto.request.OneLineUpdateRequestDto;
import com.mmc.bookduck.domain.oneline.service.OneLineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onelines")
@Tag(name = "oneLine", description = "한줄평 관련 API입니다.")
public class OneLineController {
    private final OneLineService oneLineService;

    @PostMapping
    @Operation(summary = "한줄평 생성", description = "한줄평을 생성합니다.")
    public ResponseEntity<?> createOneLine(@Valid @RequestBody OneLineCreateRequestDto requestDto){
        oneLineService.createOneLine(requestDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{onelineId}")
    @Operation(summary = "한줄평 수정", description = "한줄평을 수정합니다.")
    public ResponseEntity<?> updateOneLine(@PathVariable("onelineId") final Long oneLineId,
                                           @Valid @RequestBody OneLineUpdateRequestDto requestDto){
        oneLineService.updateOneLine(oneLineId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{onelineId}")
    @Operation(summary = "한줄평 삭제", description = "한줄평을 삭제합니다.")
    public ResponseEntity<?> deleteOneLine(@PathVariable("onelineId") final Long oneLineId){
        oneLineService.deleteOneLine(oneLineId);
        return ResponseEntity.ok().build();
    }

}
