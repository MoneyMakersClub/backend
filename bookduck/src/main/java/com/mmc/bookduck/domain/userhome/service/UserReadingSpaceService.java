package com.mmc.bookduck.domain.userhome.service;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.userhome.dto.common.HomeCardDto;
import com.mmc.bookduck.domain.userhome.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.userhome.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.userhome.dto.response.ReadingSpaceResponseDto;
import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import com.mmc.bookduck.domain.userhome.repository.UserHomeRepository;
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
    private final UserHomeRepository userHomeRepository;
    private final HomeCardService homeCardService;
    private final UserHomeService userHomeService;
    private final HomeCardConverter homeCardConverter;  // HomeCardConverter 주입

    @Transactional(readOnly = true)
    public ReadingSpaceResponseDto getUserReadingSpace(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        UserHome userHome = userHomeService.getUserHomeOfUser(user);
        List<HomeCard> homeCards = homeCardService.getAllHomeCardsOfUserHome(userHome);
        String nickname = user.getNickname();

        // HomeCardConverter를 사용하여 HomeCard를 HomeCardDto로 변환
        List<HomeCardDto> homeCardDtos = homeCards.stream()
                .map(homeCard -> homeCardConverter.mapToHomeCardDto(homeCard, nickname))
                .collect(Collectors.toList());

        return new ReadingSpaceResponseDto(homeCardDtos);
    }

    public HomeCardDto addHomeCardToReadingSpace(HomeCardRequestDto requestDto) {
        User user = userService.getCurrentUser();
        UserHome userHome = userHomeService.getUserHomeOfUser(user);
        List<HomeCard> homeCards = homeCardService.getAllHomeCardsOfUserHome(userHome);

        // 새로운 HomeCard를 추가하고 HomeCardDto로 변환
        HomeCard homeCard = homeCardService.addHomeCard(requestDto, userHome, homeCards.size());
        return homeCardConverter.mapToHomeCardDto(homeCard, user.getNickname());
    }

    public void updateReadingSpace(ReadingSpaceUpdateRequestDto requestDto) {
        User user = userService.getCurrentUser();
        UserHome userHome = userHomeService.getUserHomeOfUser(user);
        homeCardService.updateHomeCards(requestDto, userHome);
    }
}
