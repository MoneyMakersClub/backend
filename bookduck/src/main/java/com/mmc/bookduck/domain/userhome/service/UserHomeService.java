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
import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import com.mmc.bookduck.domain.userhome.repository.HomeCardRepository;
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
    private final HomeCardRepository homeCardRepository;
    private final ArchiveService archiveService;
    private final OneLineRepository oneLineRepository;
    private final BookInfoService bookInfoService;

    public UserReadingSpaceResponseDto getUserReadingSpace(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        List<HomeCard> homeCards = getAllHomeCardsOfUser(user);
        String nickname = user.getNickname();
        List<HomeCardDto> homeCardDtos = homeCards.stream()
                .map((HomeCard homeCard) -> mapToHomeCardDto(homeCard, nickname))
                .collect(Collectors.toList());
        return new UserReadingSpaceResponseDto(homeCardDtos);
    }

    private HomeCardDto mapToHomeCardDto(HomeCard homeCard, String nickname) {
        return switch (homeCard.getCardType()) {
            case EXCERPT -> createExcerptCardDto(homeCard);
            case ONELINE -> createOneLineCardDto(homeCard);
            case BOOK_WITH_MEMO -> createBookWithMemoCardDto(homeCard);
            case BOOK_WITH_SONG -> createBookWithSongCardDto(homeCard, nickname);
        };
    }

    private ExcerptCardDto createExcerptCardDto(HomeCard homeCard) {
        Excerpt excerpt = archiveService.findArchiveByType(homeCard.getResourceId1(), ArchiveType.EXCERPT).getExcerpt();
        return new ExcerptCardDto(
                homeCard.getHomeCardId(),
                excerpt.getExcerptId(),
                excerpt.getUserBook().getBookInfo().getTitle(),
                excerpt.getUserBook().getBookInfo().getAuthor(),
                excerpt.getPageNumber(),
                excerpt.getExcerptContent()
        );
    }

    private OneLineCardDto createOneLineCardDto(HomeCard homeCard) {
        OneLine oneLine = oneLineRepository.findById(homeCard.getResourceId1()).get(); //TODO: 추후 수정!!
        return new OneLineCardDto(
                homeCard.getHomeCardId(),
                oneLine.getOneLineId(),
                oneLine.getUserBook().getBookInfo().getTitle(),
                oneLine.getUserBook().getBookInfo().getAuthor(),
                oneLine.getUserBook().getRating(),
                oneLine.getOneLineContent()
        );
    }

    private BookWithMemoCardDto createBookWithMemoCardDto(HomeCard homeCard) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeCard.getResourceId1());
        BookInfo bookInfo2 = homeCard.getResourceId2() != null ? bookInfoService.getBookInfoById(homeCard.getResourceId2()) : null;
        return new BookWithMemoCardDto(
                homeCard.getHomeCardId(),
                bookInfo1.getBookInfoId(),
                bookInfo2 != null ? bookInfo2.getBookInfoId() : null,
                bookInfo1.getImgPath(),
                bookInfo2 != null ? bookInfo2.getImgPath() : null,
                "MEMO",
                homeCard.getText1(),
                homeCard.getText2()
        );
    }

    private BookWithSongCardDto createBookWithSongCardDto(HomeCard homeCard, String nickname) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeCard.getResourceId1());
        BookInfo bookInfo2 = homeCard.getResourceId2() != null ? bookInfoService.getBookInfoById(homeCard.getResourceId2()) : null;
        return new BookWithSongCardDto(
                homeCard.getHomeCardId(),
                bookInfo1.getBookInfoId(),
                bookInfo2 != null ? bookInfo2.getBookInfoId() : null,
                bookInfo1.getImgPath(),
                bookInfo2 != null ? bookInfo2.getImgPath() : null,
                "SONG",
                homeCard.getText1(),
                homeCard.getText2(),
                nickname
        );
    }

    @Transactional(readOnly = true)
    public List<HomeCard> getAllHomeCardsOfUser(User user) {
        UserHome userHome = userHomeRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USERHOME_NOT_FOUND));
        return homeCardRepository.findAllByUserHome(userHome);
    }

    public HomeCard addCard() {
        return null;
    }
}