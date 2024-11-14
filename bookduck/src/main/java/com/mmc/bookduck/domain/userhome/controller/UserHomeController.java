package com.mmc.bookduck.domain.userhome.controller;

import com.mmc.bookduck.domain.userhome.dto.common.HomeCardDto;
import com.mmc.bookduck.domain.userhome.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.userhome.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.userhome.service.UserHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserHome", description = "UserHome 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/readingspace")
public class UserHomeController {
    private final UserHomeService userHomeService;

    @Operation(summary = "내 리딩스페이스에 카드 추가", description = "내 리딩스페이스에 카드를 추가합니다.")
    @PostMapping
    public ResponseEntity<?> addHomeCardToReadingSpace(@RequestBody @Valid HomeCardRequestDto requestDto) {
        HomeCardDto homeCardDto = userHomeService.addHomeCardToReadingSpace(requestDto);
        return ResponseEntity.ok().body(homeCardDto);
    }

    @Operation(summary = "내 리딩스페이스 편집", description = "내 리딩스페이스를 편집합니다.")
    @PatchMapping
    public ResponseEntity<?> updateReadingSpace(@RequestBody @Valid ReadingSpaceUpdateRequestDto requestDto) {
        userHomeService.updateReadingSpace(requestDto);
        return ResponseEntity.ok().build();
    }
}
