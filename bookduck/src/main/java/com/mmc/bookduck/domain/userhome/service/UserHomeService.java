package com.mmc.bookduck.domain.userhome.service;

import com.mmc.bookduck.domain.archive.entity.ArchiveType;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.service.ArchiveService;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.service.BookInfoService;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.oneline.repository.OneLineRepository;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.userhome.dto.common.*;
import com.mmc.bookduck.domain.userhome.dto.response.UserReadingSpaceResponseDto;
import com.mmc.bookduck.domain.userhome.entity.HomeBlock;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import com.mmc.bookduck.domain.userhome.repository.HomeBlockRepository;
import com.mmc.bookduck.domain.userhome.repository.UserHomeRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHomeService {
    private final UserService userService;
    private final UserHomeRepository userHomeRepository;
    private final HomeBlockRepository homeBlockRepository;
    private final ArchiveService archiveService;
    private final OneLineRepository oneLineRepository;
    private final BookInfoService bookInfoService;

    public UserReadingSpaceResponseDto getUserReadingSpace(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        List<HomeBlock> homeBlocks = getAllHomeBlocksOfUser(user);
        String nickname = user.getNickname();
        List<HomeBlockDto> homeBlockDtos = homeBlocks.stream()
                .map((HomeBlock homeBlock) -> mapToHomeBlockDto(homeBlock, nickname))
                .collect(Collectors.toList());
        return new UserReadingSpaceResponseDto(homeBlockDtos);
    }

    private HomeBlockDto mapToHomeBlockDto(HomeBlock homeBlock, String nickname) {
        return switch (homeBlock.getBlockType()) {
            case EXCERPT -> createExcerptBlockDto(homeBlock);
            case ONELINE -> createOneLineBlockDto(homeBlock);
            case BOOK_WITH_MEMO -> createBookWithMemoBlockDto(homeBlock);
            case BOOK_WITH_SONG -> createBookWithSongBlockDto(homeBlock, nickname);
        };
    }

    private ExcerptBlockDto createExcerptBlockDto(HomeBlock homeBlock) {
        Excerpt excerpt = archiveService.findArchiveByType(homeBlock.getResourceId1(), ArchiveType.EXCERPT).getExcerpt();
        return new ExcerptBlockDto(
                homeBlock.getHomeBlockId(),
                excerpt.getExcerptId(),
                excerpt.getUserBook().getBookInfo().getTitle(),
                excerpt.getUserBook().getBookInfo().getAuthor(),
                excerpt.getPageNumber(),
                excerpt.getExcerptContent()
        );
    }

    private OneLineBlockDto createOneLineBlockDto(HomeBlock homeBlock) {
        OneLine oneLine = oneLineRepository.findById(homeBlock.getResourceId1()).get(); //TODO: 추후 수정!!
        return new OneLineBlockDto(
                homeBlock.getHomeBlockId(),
                oneLine.getOneLineId(),
                oneLine.getUserBook().getBookInfo().getTitle(),
                oneLine.getUserBook().getBookInfo().getAuthor(),
                oneLine.getUserBook().getRating(),
                oneLine.getOneLineContent()
        );
    }

    private BookWithMemoBlockDto createBookWithMemoBlockDto(HomeBlock homeBlock) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeBlock.getResourceId1());
        BookInfo bookInfo2 = homeBlock.getResourceId2() != null ? bookInfoService.getBookInfoById(homeBlock.getResourceId2()) : null;
        return new BookWithMemoBlockDto(
                homeBlock.getHomeBlockId(),
                bookInfo1.getBookInfoId(),
                bookInfo2 != null ? bookInfo2.getBookInfoId() : null,
                bookInfo1.getImgPath(),
                bookInfo2 != null ? bookInfo2.getImgPath() : null,
                "MEMO",
                homeBlock.getText1(),
                homeBlock.getText2()
        );
    }

    private BookWithSongBlockDto createBookWithSongBlockDto(HomeBlock homeBlock, String nickname) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeBlock.getResourceId1());
        BookInfo bookInfo2 = homeBlock.getResourceId2() != null ? bookInfoService.getBookInfoById(homeBlock.getResourceId2()) : null;
        return new BookWithSongBlockDto(
                homeBlock.getHomeBlockId(),
                bookInfo1.getBookInfoId(),
                bookInfo2 != null ? bookInfo2.getBookInfoId() : null,
                bookInfo1.getImgPath(),
                bookInfo2 != null ? bookInfo2.getImgPath() : null,
                "SONG",
                homeBlock.getText1(),
                homeBlock.getText2(),
                nickname
        );
    }

    @Transactional(readOnly = true)
    public List<HomeBlock> getAllHomeBlocksOfUser(User user) {
        UserHome userHome = userHomeRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USERHOME_NOT_FOUND));
        return homeBlockRepository.findAllByUserHome(userHome);
    }
}