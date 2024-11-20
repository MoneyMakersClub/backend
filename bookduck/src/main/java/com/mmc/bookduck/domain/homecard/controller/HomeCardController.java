package com.mmc.bookduck.domain.homecard.controller;

import com.mmc.bookduck.domain.archive.service.ExcerptService;
import com.mmc.bookduck.domain.oneline.service.OneLineService;
import com.mmc.bookduck.domain.homecard.dto.common.HomeCardDto;
import com.mmc.bookduck.domain.homecard.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.homecard.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.homecard.service.UserReadingSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "HomeCard", description = "HomeCard 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/readingspace")
public class HomeCardController {
    private final UserReadingSpaceService readingSpaceService;
    private final ExcerptService excerptService;
    private final OneLineService oneLineService;

    @Operation(summary = "내 리딩스페이스에 카드 추가", description = "내 리딩스페이스에 카드를 추가합니다.")
    @PostMapping
    public ResponseEntity<?> addHomeCardToReadingSpace(@RequestBody @Valid HomeCardRequestDto requestDto) {
        HomeCardDto homeCardDto = readingSpaceService.addHomeCardToReadingSpace(requestDto);
        return ResponseEntity.ok().body(homeCardDto);
    }

    @Operation(summary = "내 리딩스페이스 편집", description = "내 리딩스페이스를 편집합니다.")
    @PatchMapping
    public ResponseEntity<?> updateReadingSpace(@RequestBody @Valid ReadingSpaceUpdateRequestDto requestDto) {
        readingSpaceService.updateReadingSpace(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "발췌 카드 추가 시 발췌 검색", description = "발췌 카드 추가 시 나의 발췌를 검색합니다.")
    @GetMapping("/excerpts/search")
    public ResponseEntity<?> searchExcerptsFromReadingSpace(@RequestParam final String keyword,
                                                            @PageableDefault final Pageable pageable) {
        return ResponseEntity.ok().body(excerptService.searchExcerptsFromReadingSpace(keyword, pageable));
    }

    @Operation(summary = "한줄평 카드 추가 시 한줄평 검색", description = "한줄평 카드 추가 시 나의 한줄평을 검색합니다.")
    @GetMapping("/onelines/search")
    public ResponseEntity<?> searchOneLinesFromReadingSpace(@RequestParam final String keyword,
                                                            @PageableDefault final Pageable pageable) {
        return ResponseEntity.ok().body(oneLineService.searchOneLineFromReadingSpace(keyword, pageable));
    }
}
