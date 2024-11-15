package com.mmc.bookduck.domain.userhome.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.service.ExcerptService;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.service.BookInfoService;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.oneline.service.OneLineService;
import com.mmc.bookduck.domain.userhome.dto.common.*;
import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeCardConverter {
    private final ExcerptService excerptService;
    private final OneLineService oneLineService;
    private final BookInfoService bookInfoService;

    public HomeCardDto mapToHomeCardDto(HomeCard homeCard, String nickname) {
        return switch (homeCard.getCardType()) {
            case EXCERPT -> convertToExcerptCardDto(homeCard);
            case ONELINE -> convertToOneLineCardDto(homeCard);
            case BOOK_WITH_MEMO -> convertToBookWithMemoCardDto(homeCard);
            case BOOK_WITH_SONG -> convertToBookWithSongCardDto(homeCard, nickname);
        };
    }

    private ExcerptCardDto convertToExcerptCardDto(HomeCard homeCard) {
        Excerpt excerpt = excerptService.getExcerptById(homeCard.getResourceId1());
        return new ExcerptCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                excerpt.getUserBook().getBookInfo().getTitle(), excerpt.getUserBook().getBookInfo().getAuthor(),
                excerpt.getPageNumber(), excerpt.getExcerptContent());
    }

    private OneLineCardDto convertToOneLineCardDto(HomeCard homeCard) {
        OneLine oneLine = oneLineService.getOneLineById(homeCard.getResourceId1());
        return new OneLineCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                oneLine.getUserBook().getBookInfo().getTitle(), oneLine.getUserBook().getBookInfo().getAuthor(),
                oneLine.getUserBook().getRating(), oneLine.getOneLineContent());
    }

    private BookWithMemoCardDto convertToBookWithMemoCardDto(HomeCard homeCard) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeCard.getResourceId1());
        BookInfo bookInfo2 = Optional.ofNullable(homeCard.getResourceId2())
                .map(bookInfoService::getBookInfoById)
                .orElse(null);
        return new BookWithMemoCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                bookInfo1.getImgPath(), bookInfo2 != null ? bookInfo2.getImgPath() : null, homeCard.getText1());
    }

    private BookWithSongCardDto convertToBookWithSongCardDto(HomeCard homeCard, String nickname) {
        BookInfo bookInfo1 = bookInfoService.getBookInfoById(homeCard.getResourceId1());
        BookInfo bookInfo2 = Optional.ofNullable(homeCard.getResourceId2())
                .map(bookInfoService::getBookInfoById)
                .orElse(null);
        return new BookWithSongCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                bookInfo1.getImgPath(), bookInfo2 != null ? bookInfo2.getImgPath() : null, homeCard.getText1(), homeCard.getText2(), nickname);
    }
}