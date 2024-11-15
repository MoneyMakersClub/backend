package com.mmc.bookduck.domain.userhome.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.userhome.dto.common.HomeCardUpdateUnitDto;
import com.mmc.bookduck.domain.userhome.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.userhome.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.userhome.entity.CardType;
import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import com.mmc.bookduck.domain.userhome.repository.HomeCardRepository;
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
    public List<HomeCard> getAllHomeCardsOfUserHome(UserHome userHome) {
        return homeCardRepository.findAllByUserHome(userHome);
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

    public void updateHomeCards(ReadingSpaceUpdateRequestDto requestDto, UserHome userHome) {

        Map<Long, HomeCard> currentHomeCardsMap = getAllHomeCardsOfUserHome(userHome).stream()
                .collect(Collectors.toMap(HomeCard::getHomeCardId, homeCard -> homeCard));

        List<HomeCard> cardsToUpdate = new ArrayList<>();

        // 업데이트할 카드 목록 처리
        for (HomeCardUpdateUnitDto cardDto : requestDto.homeCardUpdateUnitDtos()) {
            HomeCard existingCard = currentHomeCardsMap.get(cardDto.homeCardId());

            if (existingCard != null) {
                existingCard.updateCardIndex(cardDto.cardIndex());
                cardsToUpdate.add(existingCard);
                currentHomeCardsMap.remove(cardDto.homeCardId());
            } else {
                throw new CustomException(ErrorCode.HOMECARD_NOT_FOUND);
            }
        }

        List<HomeCard> cardsToDelete = new ArrayList<>(currentHomeCardsMap.values());
        userHome.updateLastModifiedAt();

        // 삭제할 카드와 업데이트할 카드를 처리
        homeCardRepository.deleteAll(cardsToDelete);
        homeCardRepository.saveAll(cardsToUpdate);
    }

    // Excerpt 카드 삭제
    public void deleteHomeCardsByExcerpt(UserHome userHome, Excerpt excerpt) {
        List<HomeCard> cards = homeCardRepository.findAllByUserHomeAndCardTypeAndResourceId1(userHome, CardType.EXCERPT, excerpt.getExcerptId());
        homeCardRepository.deleteAll(cards);
    }

    // OneLine 카드 삭제
    public void deleteHomeCardsByOneLine(UserHome userHome, OneLine oneLine) {
        List<HomeCard> cards = homeCardRepository.findAllByUserHomeAndCardTypeAndResourceId1(userHome, CardType.ONELINE, oneLine.getOneLineId());
        homeCardRepository.deleteAll(cards);
    }

    // UserBook 카드 삭제
    public void deleteHomeCardsByUserBook(UserHome userHome, UserBook userBook) {
        List<CardType> cardTypes = Arrays.asList(CardType.BOOK_WITH_MEMO, CardType.BOOK_WITH_SONG);
        List<HomeCard> cards = homeCardRepository.findAllByUserHomeAndCardTypesAndResourceId1Or2(userHome, cardTypes, userBook.getUserBookId());
        homeCardRepository.deleteAll(cards);
    }
}
