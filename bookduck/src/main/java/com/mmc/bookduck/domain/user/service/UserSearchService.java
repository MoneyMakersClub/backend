package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.friend.service.FriendService;
import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;
import com.mmc.bookduck.domain.item.service.UserItemService;
import com.mmc.bookduck.domain.user.dto.common.UserUnitDto;
import com.mmc.bookduck.domain.user.dto.response.UserListResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mmc.bookduck.global.common.EscapeSpecialCharactersService.escapeSpecialCharacters;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;
    private final UserItemService userItemService;
    private final FriendService friendService;

    // 유저 검색
    public UserListResponseDto searchUsers(String keyword, Pageable pageable) {
        // 키워드의 이스케이프 처리
        String escapedWord = escapeSpecialCharacters(keyword);
        Page<User> userPage = getSearchedUserPage(escapedWord, pageable);

        Page<UserUnitDto> userUnitDtoPage = userPage.map(user -> {
            List<ItemEquippedUnitDto> userItems = userItemService.getUserItemEquippedListOfUser(user);
            boolean isFriend = friendService.isFriendWithCurrentUser(user);
            return UserUnitDto.from(user, userItems, isFriend);
        });
        return UserListResponseDto.from(userUnitDtoPage);
    }

    @Transactional(readOnly = true)
    public Page<User> getSearchedUserPage(String keyword, Pageable pageable) {
        Pageable sortedByNickname = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("nickname").ascending()
        );
        return userRepository.searchAllByNicknameStartingWith(keyword, sortedByNickname);
    }
}
