package com.mmc.bookduck.domain.homecard.service;

import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.homecard.dto.common.HomeCardDto;
import com.mmc.bookduck.domain.homecard.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.homecard.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.homecard.dto.response.ReadingSpaceResponseDto;
import com.mmc.bookduck.domain.homecard.entity.HomeCard;
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
public class UserReadingSpaceService {
    private final UserService userService;
    private final HomeCardService homeCardService;
    private final HomeCardConverter homeCardConverter;

    @Transactional(readOnly = true)
    public ReadingSpaceResponseDto getUserReadingSpace(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        User loginedUser = userService.getCurrentUserOrNull();

        boolean isCurrentUser = loginedUser != null && loginedUser.getUserId().equals(user.getUserId());
        String nickname = user.getNickname();

        List<HomeCardDto> homeCardDtos = homeCardService.getAllHomeCardsOfUser(user).stream()
                .filter(homeCard -> canViewHomeCard(homeCard, isCurrentUser))
                .map(homeCard -> homeCardConverter.mapToHomeCardDto(homeCard, nickname))
                .collect(Collectors.toList());

        return new ReadingSpaceResponseDto(homeCardDtos);
    }

    private boolean canViewHomeCard(HomeCard homeCard, boolean isCurrentUser) {
        if (isCurrentUser) return true;

        // 다른 유저의 경우 Visibility가 PUBLIC인 것만 허용
        return homeCard.getExcerpt() == null || homeCard.getExcerpt().getVisibility() == Visibility.PUBLIC;
    }

    public HomeCardDto addHomeCardToReadingSpace(HomeCardRequestDto requestDto) {
        User user = userService.getCurrentUser();
        List<HomeCard> homeCards = homeCardService.getAllHomeCardsOfUser(user);
        if (homeCards.size() >= 7) {
            throw new CustomException(ErrorCode.HOMECARD_BAD_REQUEST);
        }

        // 새로운 HomeCard를 추가하고 HomeCardDto로 변환
        HomeCard homeCard = homeCardService.addHomeCard(requestDto, user, homeCards.size());
        return homeCardConverter.mapToHomeCardDto(homeCard, user.getNickname());
    }

    public void updateReadingSpace(ReadingSpaceUpdateRequestDto requestDto) {
        User user = userService.getCurrentUser();
        homeCardService.updateHomeCards(requestDto, user);
    }
}
