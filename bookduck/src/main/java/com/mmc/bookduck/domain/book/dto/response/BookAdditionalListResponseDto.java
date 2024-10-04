package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookAdditionalUnitDto;
import jakarta.validation.constraints.NegativeOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookAdditionalListResponseDto {
    private List<BookAdditionalUnitDto> list;

    @Builder
    public BookAdditionalListResponseDto(List<BookAdditionalUnitDto> list){
        this.list = list;
    }
}
