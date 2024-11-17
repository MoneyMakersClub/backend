package com.mmc.bookduck.domain.homecard.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.homecard.dto.common.*;
import com.mmc.bookduck.domain.homecard.entity.HomeCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeCardConverter {

    public HomeCardDto mapToHomeCardDto(HomeCard homeCard, String nickname) {
        return switch (homeCard.getCardType()) {
            case EXCERPT -> convertToExcerptCardDto(homeCard);
            case ONELINE -> convertToOneLineCardDto(homeCard);
            case BOOK_WITH_MEMO -> convertToBookWithMemoCardDto(homeCard);
            case BOOK_WITH_SONG -> convertToBookWithSongCardDto(homeCard, nickname);
        };
    }

    private ExcerptCardDto convertToExcerptCardDto(HomeCard homeCard) {
        Excerpt excerpt = homeCard.getExcerpt();
        return new ExcerptCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                excerpt.getUserBook().getBookInfo().getTitle(), excerpt.getUserBook().getBookInfo().getAuthor(),
                excerpt.getPageNumber(), excerpt.getExcerptContent());
    }

    private OneLineCardDto convertToOneLineCardDto(HomeCard homeCard) {
        OneLine oneLine = homeCard.getOneLine();
        return new OneLineCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                oneLine.getUserBook().getBookInfo().getTitle(), oneLine.getUserBook().getBookInfo().getAuthor(),
                oneLine.getUserBook().getRating(), oneLine.getOneLineContent());
    }

    private BookWithMemoCardDto convertToBookWithMemoCardDto(HomeCard homeCard) {
        UserBook userBook1 = homeCard.getUserBook1();
        UserBook userBook2 = homeCard.getUserBook2();
        return new BookWithMemoCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                userBook1.getBookInfo().getImgPath(), userBook2 != null ? userBook2.getBookInfo().getImgPath() : null, homeCard.getText1());
    }

    private BookWithSongCardDto convertToBookWithSongCardDto(HomeCard homeCard, String nickname) {
        UserBook userBook1 = homeCard.getUserBook1();
        UserBook userBook2 = homeCard.getUserBook2();
        return new BookWithSongCardDto(homeCard.getHomeCardId(), homeCard.getCardIndex(), homeCard.getCardType(),
                userBook1.getBookInfo().getImgPath(), userBook2 != null ? userBook2.getBookInfo().getImgPath() : null, homeCard.getText1(), homeCard.getText2(), homeCard.getText3(), nickname);
    }
}