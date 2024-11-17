package com.mmc.bookduck.domain.homecard.service;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.homecard.dto.common.HomeCardDto;
import com.mmc.bookduck.domain.homecard.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.homecard.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.homecard.dto.response.ReadingSpaceResponseDto;
import com.mmc.bookduck.domain.homecard.entity.HomeCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReadingSpaceService {
    private final UserService userService;
    private final HomeCardService homeCardService;
    private final HomeCardConverter homeCardConverter;

    @Transactional(readOnly = true)
    public ReadingSpaceResponseDto getUserReadingSpace(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        User currentUser = userService.getCurrentUser();
        List<HomeCard> homeCards;

        if (user.equals(currentUser)) {
            // 자신일 경우 visibility 무관하게 모든 HomeCard 가져오기
            homeCards = homeCardService.getAllHomeCardsOfUser(user);
        } else {
            // 다른 사용자일 경우 PUBLIC HomeCard만 가져오기
            homeCards = homeCardService.getPublicHomeCardsOfUser(user);
        }
        String nickname = user.getNickname();

        // HomeCardConverter를 사용하여 HomeCard를 HomeCardDto로 변환
        List<HomeCardDto> homeCardDtos = homeCards.stream()
                .map(homeCard -> homeCardConverter.mapToHomeCardDto(homeCard, nickname))
                .collect(Collectors.toList());

        return new ReadingSpaceResponseDto(homeCardDtos);
    }

    public HomeCardDto addHomeCardToReadingSpace(HomeCardRequestDto requestDto) {
        User user = userService.getCurrentUser();
        List<HomeCard> homeCards = homeCardService.getAllHomeCardsOfUser(user);

        // 새로운 HomeCard를 추가하고 HomeCardDto로 변환
        HomeCard homeCard = homeCardService.addHomeCard(requestDto, user, homeCards.size());
        return homeCardConverter.mapToHomeCardDto(homeCard, user.getNickname());
    }

    public void updateReadingSpace(ReadingSpaceUpdateRequestDto requestDto) {
        User user = userService.getCurrentUser();
        homeCardService.updateHomeCards(requestDto, user);
    }
}
