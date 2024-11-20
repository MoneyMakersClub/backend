package com.mmc.bookduck.domain.oneline.controller;

import com.mmc.bookduck.domain.oneline.dto.request.OneLineCreateRequestDto;
import com.mmc.bookduck.domain.oneline.dto.request.OneLineUpdateRequestDto;
import com.mmc.bookduck.domain.oneline.service.OneLineService;
import com.mmc.bookduck.domain.onelineLike.service.OneLineLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onelines")
@Tag(name = "OneLine", description = "한줄평 관련 API입니다.")
public class OneLineController {
    private final OneLineService oneLineService;
    private final OneLineLikeService oneLineLikeService;

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

    @PostMapping("/{onelineId}/like")
    @Operation(summary = "한줄평 좋아요", description = "한줄평에 좋아요를 추가합니다.")
    public ResponseEntity<?> createOneLineLike(@PathVariable("onelineId") final Long oneLineId) {
        oneLineLikeService.createOneLineLike(oneLineId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{oneLineId}/like")
    @Operation(summary = "한줄평 좋아요 취소", description = "한줄평의 좋아요를 취소합니다.")
    public ResponseEntity<?> deleteOneLineLike(@PathVariable("oneLineId") final Long oneLineId) {
        oneLineLikeService.deleteOneLineLike(oneLineId);
        return ResponseEntity.ok().build();
    }

}
