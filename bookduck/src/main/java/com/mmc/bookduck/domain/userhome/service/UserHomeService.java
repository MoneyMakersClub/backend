package com.mmc.bookduck.domain.userhome.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.service.ExcerptService;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.oneline.service.OneLineService;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.userhome.dto.common.HomeCardUpdateUnitDto;
import com.mmc.bookduck.domain.userhome.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.userhome.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import com.mmc.bookduck.domain.userhome.repository.HomeCardRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHomeService {
    private final HomeCardRepository homeCardRepository;
    private final ExcerptService excerptService;
    private final OneLineService oneLineService;
    private final UserBookService userBookService;

    @Transactional(readOnly = true)
    public List<HomeCard> getAllHomeCardsOfUser(User user) {
        return homeCardRepository.findAllByUserOrderByCardIndexAsc(user);
    }

    public HomeCard addHomeCard(HomeCardRequestDto requestDto, User user, long cardIndex) {
        HomeCard homeCard = HomeCard.builder()
                .cardType(requestDto.cardType())
                .cardIndex(cardIndex)
                .text1(requestDto.text1())
                .text2(requestDto.text2())
                .text3(requestDto.text3())
                .user(user)
                .build();
        switch (requestDto.cardType()) {
            case EXCERPT -> {
                Excerpt excerpt = excerptService.getExcerptById(requestDto.resourceId1());
                homeCard.setExcerpt(excerpt);
            }
            case ONELINE -> {
                OneLine oneLine = oneLineService.getOneLineById(requestDto.resourceId1());
                homeCard.setOneLine(oneLine);
            }
            default -> {
                UserBook userBook1 = userBookService.getUserBookById(requestDto.resourceId1());
                UserBook userBook2 = Optional.ofNullable(requestDto.resourceId2())
                        .map(userBookService::getUserBookById)
                        .orElse(null);
                homeCard.setUserBook(userBook1, userBook2);
            }
        }
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
}
