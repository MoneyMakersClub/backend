package com.mmc.bookduck.domain.userhome.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserHome", description = "UserHome 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class UserHomeController {

    @Operation(summary = "리딩스페이스 편집", description = "리딩스페이스를 편집합니다.")
    @PostMapping("/readingspace")
    public ResponseEntity<?> getUserReadingSpace(@PathVariable final Long userId) {
        return null;
    }
}
