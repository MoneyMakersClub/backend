package com.mmc.bookduck.domain.item.controller;

import com.mmc.bookduck.domain.item.dto.request.UserItemUpdateRequestDto;
import com.mmc.bookduck.domain.item.service.UserItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Item", description = "Item 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class UserItemController {
    private final UserItemService userItemService;

    @Operation(summary = "캐릭터 꾸미기 아이템 조회", description = "캐릭터 꾸미기 아이템을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getUserItemCloset() {
        return ResponseEntity.ok(userItemService.getUserItemCloset());
    }

    @Operation(summary = "캐릭터 아이템 장착상태 변경", description = "캐릭터 아이템 장착상태를 변경합니다.")
    @PatchMapping
    public ResponseEntity<?> updateUserItemsEquippedStatus(@RequestBody @Valid UserItemUpdateRequestDto requestDto) {
        userItemService.updateUserItemsEquippedStatus(requestDto);
        return ResponseEntity.ok().build();
    }
}
