package com.mmc.bookduck.domain.user.dto.common;

public record MonthlyBookCountUnitDto(
        int month,
        long bookCount
) {
}
