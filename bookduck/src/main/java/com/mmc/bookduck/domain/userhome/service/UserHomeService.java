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
import com.mmc.bookduck.domain.userhome.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.userhome.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.userhome.dto.response.ReadingSpaceResponseDto;
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

    @Transactional(readOnly = true)
    public ReadingSpaceResponseDto getUserReadingSpace(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        UserHome userHome = getUserHomeOfUser(user);
        List<HomeCard> homeCards = getAllHomeCardsOfUserHome(userHome);
        String nickname = user.getNickname();
        List<HomeCardDto> homeCardDtos = homeCards.stream()
                .map((HomeCard homeCard) -> mapToHomeCardDto(homeCard, nickname))
                .collect(Collectors.toList());
        return new ReadingSpaceResponseDto(homeCardDtos);
    }

    @Transactional(readOnly = true)
    public HomeCardDto mapToHomeCardDto(HomeCard homeCard, String nickname) {
        return switch (homeCard.getCardType()) {
            case EXCERPT -> convertToExcerptCardDto(homeCard);
            case ONELINE -> convertToOneLineCardDto(homeCard);
            case BOOK_WITH_MEMO -> convertToBookWithMemoCardDto(homeCard);
            case BOOK_WITH_SONG -> convertToBookWithSongCardDto(homeCard, nickname);
        };
    }

    @Transactional(readOnly = true)
    public ExcerptCardDto convertToExcerptCardDto(HomeCard homeCard) {
        Excerpt excerpt = archiveService.findArchiveByType(homeCard.getResourceId1(), ArchiveType.EXCERPT).getExcerpt();
        return new ExcerptCardDto(
                homeCard.getHomeCardId(),
                homeCard.getCardIndex(),
                excerpt.getExcerptId(),
                excerpt.getUserBook().getBookInfo().getTitle(),
                excerpt.getUserBook().getBookInfo().getAuthor(),
                excerpt.getPageNumber(),
                excerpt.getExcerptContent()
        );
    }

    @Transactional(readOnly = true)
    public OneLineCardDto convertToOneLineCardDto(HomeCard homeCard) {
        OneLine oneLine = oneLineRepository.findById(homeCard.getResourceId1()).get(); //TODO: 추후 수정!!
        return new OneLineCardDto(
                homeCard.getHomeCardId(),
                homeCard.getCardIndex(),
                oneLine.getOneLineId(),
                oneLine.getUserBook().getBookInfo().getTitle(),
                oneLine.getUserBook().getBookInfo().getAuthor(),
                oneLine.getUserBook().getRating(),
                oneLine.getOneLineContent()
        );
    }

    @Transactional(readOnly = true)
    public BookWithMemoCardDto convertToBookWithMemoCardDto(HomeCard homeCard) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeCard.getResourceId1());
        BookInfo bookInfo2 = homeCard.getResourceId2() != null ? bookInfoService.getBookInfoById(homeCard.getResourceId2()) : null;
        return new BookWithMemoCardDto(
                homeCard.getHomeCardId(),
                homeCard.getCardIndex(),
                bookInfo1.getBookInfoId(),
                bookInfo2 != null ? bookInfo2.getBookInfoId() : null,
                bookInfo1.getImgPath(),
                bookInfo2 != null ? bookInfo2.getImgPath() : null,
                "MEMO",
                homeCard.getText1(),
                homeCard.getText2()
        );
    }

    @Transactional(readOnly = true)
    public BookWithSongCardDto convertToBookWithSongCardDto(HomeCard homeCard, String nickname) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeCard.getResourceId1());
        BookInfo bookInfo2 = homeCard.getResourceId2() != null ? bookInfoService.getBookInfoById(homeCard.getResourceId2()) : null;
        return new BookWithSongCardDto(
                homeCard.getHomeCardId(),
                homeCard.getCardIndex(),
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
    public List<HomeCard> getAllHomeCardsOfUserHome(UserHome userHome) {
        return homeCardRepository.findAllByUserHome(userHome);
    }

    @Transactional(readOnly = true)
    public UserHome getUserHomeOfUser(User user) {
        return userHomeRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USERHOME_NOT_FOUND));
    }

    public HomeCardDto addHomeCardToReadingSpace(HomeCardRequestDto requestDto) {
        User user = userService.getCurrentUser();
        UserHome userHome = getUserHomeOfUser(user);
        List<HomeCard> homeCards = getAllHomeCardsOfUserHome(userHome);
        HomeCard homeCard = addHomeCard(requestDto, userHome, homeCards.size());
        return mapToHomeCardDto(homeCard, user.getNickname());
    }

    public HomeCard addHomeCard(HomeCardRequestDto requestDto, UserHome userHome, long cardIndex) {
        HomeCard homeCard = HomeCard.builder()
                .cardType(requestDto.cardType())
                .cardIndex(cardIndex)
                .resourceId1(requestDto.resourceId1())
                .resourceId2(requestDto.resourceId2())
                .text1(requestDto.text1())
                .text2(requestDto.text2())
                .userHome(userHome)
                .build();
        return homeCardRepository.save(homeCard);
    }

    public void updateReadingSpace(ReadingSpaceUpdateRequestDto requestDto) {
    }
}