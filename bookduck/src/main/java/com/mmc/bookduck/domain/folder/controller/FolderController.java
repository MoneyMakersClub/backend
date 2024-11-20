package com.mmc.bookduck.domain.folder.controller;

import com.mmc.bookduck.domain.folder.dto.request.FolderBookOrderRequestDto;
import com.mmc.bookduck.domain.folder.dto.request.FolderRequestDto;
import com.mmc.bookduck.domain.folder.dto.response.AllFolderListResponseDto;
import com.mmc.bookduck.domain.folder.dto.response.CandidateFolderBookListResponseDto;
import com.mmc.bookduck.domain.folder.dto.response.FolderBookListResponseDto;
import com.mmc.bookduck.domain.folder.dto.response.FolderResponseDto;
import com.mmc.bookduck.domain.folder.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Folder", description = "Folder 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/folders")
public class FolderController {
    private final FolderService folderService;

    @Operation(summary = "폴더 생성", description = "폴더를 생성합니다.")
    @PostMapping
    public ResponseEntity<FolderResponseDto> createFolder(@Valid @RequestBody FolderRequestDto dto, Errors error){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(folderService.createFolder(dto, error));
    }

    @Operation(summary = "폴더 이름 수정", description = "폴더의 이름을 수정합니다.")
    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderResponseDto> updateFolder(@PathVariable final Long folderId,
                                                          @Valid @RequestBody FolderRequestDto dto, Errors error){
        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.updateFolder(folderId, dto, error));
    }

    @Operation(summary = "폴더 삭제", description = "폴더를 삭제합니다.")
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable(name = "folderId") final Long folderId){

        folderService.deleteFolder(folderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "폴더에 책 여러 개 추가", description = "폴더에 여러 개의 책을 추가합니다.")
    @PostMapping("/{folderId}/books")
    public ResponseEntity<FolderBookListResponseDto> addFolderBooks(@PathVariable(name="folderId") final Long folderId,
                                                                    @RequestBody List<Long> userBookIds){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(folderService.addFolderBooks(folderId, userBookIds));
    }


    @Operation(summary = "폴더에 추가할 책 목록 조회", description = "폴더에 추가할 수 있는 서재 책 목록을 조회합니다.")
    @GetMapping("/{folderId}/books/candidates")
    public ResponseEntity<CandidateFolderBookListResponseDto> getCandidateBooks(@PathVariable(name="folderId") final Long folderId,
                                                                                @RequestParam(name = "status", required = false) final List<String> statusList){

        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.getCandidateBooks(folderId, statusList));
    }


    @Operation(summary = "폴더에서 책 여러 개 삭제", description = "폴더에서 여러 개의 책을 삭제합니다.")
    @DeleteMapping("/{folderId}/books")
    public ResponseEntity<FolderBookListResponseDto> deleteFolderBooks(@PathVariable(name="folderId") final Long folderId,
                                                                       @RequestBody List<Long> folderBookIds){
        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.deleteFolderBooks(folderId, folderBookIds));
    }

    @Operation(summary = "전체 폴더 목록 조회", description = "현재 사용자의 전체 폴더 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<AllFolderListResponseDto> getAllFolderList(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.getAllFolderList());
    }


    @Operation(summary = "폴더별 책 목록 조회", description = "폴더에 포함된 책 목록을 조회합니다.")
    @GetMapping("/{folderId}/books")
    public ResponseEntity<FolderBookListResponseDto> getFolderBookList(@PathVariable(name="folderId") final Long folderId){

        return ResponseEntity.status(HttpStatus.OK)
                .body((folderService.getFolderBookList(folderId)));
    }


    @Operation(summary = "폴더별&상태별 책 목록 조회", description = "폴더에 포함된 책에서 특정 상태의 책 목록을 조회합니다.")
    @GetMapping("/{folderId}/books/filter")
    public ResponseEntity<FolderBookListResponseDto> getFolderBookListStatus(@PathVariable(name="folderId") final Long folderId,
                                                                             @RequestParam(name = "status") final List<String> statusList){

        return ResponseEntity.status(HttpStatus.OK)
                .body((folderService.getFolderBookListStatus(folderId, statusList)));
    }

    @Operation(summary = "폴더 책 순서 변경", description = "폴더안의 책 순서를 변경합니다.")
    @PatchMapping("/{folderId}/books/order")
    public ResponseEntity<FolderBookListResponseDto> updateFolderBookOrder(@PathVariable(name="folderId") final Long folderId,
                                                                           @RequestBody final FolderBookOrderRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body((folderService.updateFolderBookOrder(folderId, requestDto)));
    }
}
