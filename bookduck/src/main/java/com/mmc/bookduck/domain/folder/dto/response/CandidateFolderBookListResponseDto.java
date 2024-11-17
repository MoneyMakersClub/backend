package com.mmc.bookduck.domain.folder.dto.response;

import com.mmc.bookduck.domain.folder.dto.common.CandidateFolderBookDto;

import java.util.List;

public record CandidateFolderBookListResponseDto(int bookCount, List<CandidateFolderBookDto> candidateFolderBookList) {
    public static CandidateFolderBookListResponseDto from(List<CandidateFolderBookDto> candidateFolderBookList){
        return new CandidateFolderBookListResponseDto(
                candidateFolderBookList.size(),
                candidateFolderBookList
        );
    }
}