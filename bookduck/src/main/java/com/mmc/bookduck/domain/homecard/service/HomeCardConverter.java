package com.mmc.bookduck.domain.homecard.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.service.ExcerptService;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.oneline.service.OneLineService;
import com.mmc.bookduck.domain.homecard.dto.common.*;
import com.mmc.bookduck.domain.homecard.entity.HomeCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeCardConverter {
    private final ExcerptService excerptService;
    private final OneLineService oneLineService;
    private final UserBookService userBookService;

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
        UserBook userBook1 = userBookService.getUserBookById(homeCard.getResourceId1());
        UserBook userBook2 = Optional.ofNullable(homeCard.getResourceId2())
                .map(userBookService::getUserBookById)
                .orElse(null);
        return new BookWithMemoCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                userBook1.getBookInfo().getImgPath(), userBook2 != null ? userBook2.getBookInfo().getImgPath() : null, homeCard.getText1());
    }

    private BookWithSongCardDto convertToBookWithSongCardDto(HomeCard homeCard, String nickname) {
        UserBook userBook1 = userBookService.getUserBookById(homeCard.getResourceId1());
        UserBook userBook2 = Optional.ofNullable(homeCard.getResourceId2())
                .map(userBookService::getUserBookById)
                .orElse(null);
        return new BookWithSongCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                userBook1.getBookInfo().getImgPath(), userBook2 != null ? userBook2.getBookInfo().getImgPath() : null, homeCard.getText1(), homeCard.getText2(), homeCard.getText3(), nickname);
    }
}