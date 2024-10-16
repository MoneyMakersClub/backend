package com.mmc.bookduck.domain.user.dto.response;

import com.mmc.bookduck.domain.user.dto.common.UserUnitDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record UserSearchResponseDto(
    int currentPage,
    int pageSize,
    long totalElements,
    int totalPages,
    List<UserUnitDto> userList
) {

    public static UserSearchResponseDto from(Page<UserUnitDto> userUnitDtoPage) {
        return new UserSearchResponseDto(
                userUnitDtoPage.getNumber(),
                userUnitDtoPage.getSize(),
                userUnitDtoPage.getTotalElements(),
                userUnitDtoPage.getTotalPages(),
                userUnitDtoPage.getContent()
        );
    }
}
