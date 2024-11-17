package com.mmc.bookduck.domain.homecard.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.homecard.dto.common.HomeCardUpdateUnitDto;
import com.mmc.bookduck.domain.homecard.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.homecard.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.homecard.entity.CardType;
import com.mmc.bookduck.domain.homecard.entity.HomeCard;
import com.mmc.bookduck.domain.homecard.repository.HomeCardRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeCardService {
    private final HomeCardRepository homeCardRepository;

    @Transactional(readOnly = true)
    public List<HomeCard> getAllHomeCardsOfUser(User user) {
        return homeCardRepository.findAllByUserOrderByCardIndexAsc(user);
    }

    @Transactional(readOnly = true)
    public List<HomeCard> getPublicHomeCardsOfUser(User user) {
        return homeCardRepository.findAllByUserAndVisibilityByCardIndexAsc(user, Visibility.PUBLIC);
    }

    public HomeCard addHomeCard(HomeCardRequestDto requestDto, User user, long cardIndex) {
        HomeCard homeCard = HomeCard.builder()
                .cardType(requestDto.cardType())
                .cardIndex(cardIndex)
                .resourceId1(requestDto.resourceId1())
                .resourceId2(requestDto.resourceId2())
                .text1(requestDto.text1())
                .text2(requestDto.text2())
                .text3(requestDto.text3())
                .user(user)
                .build();
        return homeCardRepository.save(homeCard);
    }

    public void updateHomeCards(ReadingSpaceUpdateRequestDto requestDto, User user) {
        Map<Long, HomeCard> currentHomeCardsMap = getAllHomeCardsOfUser(user).stream()
                .collect(Collectors.toMap(HomeCard::getHomeCardId, homeCard -> homeCard));

        List<HomeCard> cardsToUpdate = new ArrayList<>();

        // 업데이트할 카드 목록 처리
        for (HomeCardUpdateUnitDto cardDto : requestDto.updatedCardList()) {
            HomeCard existingCard = currentHomeCardsMap.get(cardDto.cardId());

            if (existingCard != null) {
                existingCard.updateCardIndex(cardDto.cardIndex());
                cardsToUpdate.add(existingCard);
                currentHomeCardsMap.remove(cardDto.cardId());
            } else {
                throw new CustomException(ErrorCode.HOMECARD_NOT_FOUND);
            }
        }

        List<HomeCard> cardsToDelete = new ArrayList<>(currentHomeCardsMap.values());

        // 삭제할 카드와 업데이트할 카드를 처리
        homeCardRepository.deleteAll(cardsToDelete);
        homeCardRepository.saveAll(cardsToUpdate);
    }

    // Excerpt 카드 삭제
    public void deleteHomeCardsByExcerpt(Excerpt excerpt) {
        List<HomeCard> cards = homeCardRepository.findAllByCardTypeAndResourceId1(CardType.EXCERPT, excerpt.getExcerptId());
        homeCardRepository.deleteAll(cards);
    }

    // OneLine 카드 삭제
    public void deleteHomeCardsByOneLine(OneLine oneLine) {
        List<HomeCard> cards = homeCardRepository.findAllByCardTypeAndResourceId1(CardType.ONELINE, oneLine.getOneLineId());
        homeCardRepository.deleteAll(cards);
    }

    // UserBook 카드 삭제
    public void deleteHomeCardsByUserBook(UserBook userBook) {
        List<CardType> cardTypes = Arrays.asList(CardType.BOOK_WITH_MEMO, CardType.BOOK_WITH_SONG);
        List<HomeCard> cards = homeCardRepository.findAllByCardTypesAndResourceId1Or2(cardTypes, userBook.getUserBookId());
        homeCardRepository.deleteAll(cards);
    }
}
