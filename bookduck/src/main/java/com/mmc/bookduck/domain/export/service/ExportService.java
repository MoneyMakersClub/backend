package com.mmc.bookduck.domain.export.service;

import com.mmc.bookduck.domain.export.dto.ExportCharResponseDto;
import com.mmc.bookduck.domain.export.dto.ExportStatsResponseDto;
import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;
import com.mmc.bookduck.domain.item.entity.UserItem;
import com.mmc.bookduck.domain.item.service.UserItemService;
import com.mmc.bookduck.domain.user.dto.response.UserKeywordResponseDto;
import com.mmc.bookduck.domain.user.dto.response.UserStatisticsResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserReadingReportService;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExportService {
    private final UserService userService;
    private final UserReadingReportService userReadingReportService;
    private final UserItemService userItemService;

    public ExportCharResponseDto getCharExportInfo(){
        User user = userService.getCurrentUser();
        String nickname = user.getNickname();
        // UserStatisticsResponseDto를 통해 duckTitle 재사용
        UserStatisticsResponseDto userStatistics = userReadingReportService.getUserStatistics(user.getUserId());
        String duckTitle = userStatistics.duckTitle();
        UserKeywordResponseDto keywordResponse = userReadingReportService.getUserKeywordWithLimit(user.getUserId(), 3);
        List<ItemEquippedUnitDto> userItemEquipped = userItemService.getUserItemEquippedListOfUser(user);
        return new ExportCharResponseDto(nickname, duckTitle, keywordResponse, userItemEquipped);
    }

    public ExportStatsResponseDto getStatsExportInfo(){
        return new ExportStatsResponseDto();
    }
}
