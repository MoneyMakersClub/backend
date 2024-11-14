package com.mmc.bookduck.domain.userhome.service;

import com.mmc.bookduck.domain.archive.service.ArchiveService;
import com.mmc.bookduck.domain.book.service.BookInfoService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHomeService {
    private final UserService userService;
    private final UserHomeRepository userHomeRepository;
    private final HomeCardRepository homeCardRepository;
    private final HomeCardConverter homeCardConverter;  // HomeCardConverter 주입

    @Transactional(readOnly = true)
    public ReadingSpaceResponseDto getUserReadingSpace(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        UserHome userHome = getUserHomeOfUser(user);
        List<HomeCard> homeCards = getAllHomeCardsOfUserHome(userHome);
        String nickname = user.getNickname();

        // HomeCardConverter를 사용하여 HomeCard를 HomeCardDto로 변환
        List<HomeCardDto> homeCardDtos = homeCards.stream()
                .map(homeCard -> homeCardConverter.mapToHomeCardDto(homeCard, nickname))
                .collect(Collectors.toList());

        return new ReadingSpaceResponseDto(homeCardDtos);
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

        // 새로운 HomeCard를 추가하고 HomeCardDto로 변환
        HomeCard homeCard = addHomeCard(requestDto, userHome, homeCards.size());
        return homeCardConverter.mapToHomeCardDto(homeCard, user.getNickname());
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
        UserHome userHome = getUserHomeOfUser(userService.getCurrentUser());

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
        userHome.updateLastModifiedAt(LocalDateTime.now());

        // 삭제할 카드와 업데이트할 카드를 처리
        homeCardRepository.deleteAll(cardsToDelete);
        homeCardRepository.saveAll(cardsToUpdate);
    }
}
