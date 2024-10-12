package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.user.dto.common.UserUnitDto;
import com.mmc.bookduck.domain.user.dto.response.UserSearchResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserStatus;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mmc.bookduck.global.common.EscapeSpecialCharactersService.escapeSpecialCharacters;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser() throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_AUTHENTICATED));
        validateActiveUserStatus(user);
        return user;
    }

    @Transactional(readOnly = true)
    public User getActiveUserByEmail(String email) throws CustomException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        validateActiveUserStatus(user);
        return user;
    }

    @Transactional(readOnly = true)
    public User getActiveUserByUserId(Long userId) throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        validateActiveUserStatus(user);
        return user;
    }

    // 사용자가 활성 상태임을 검증
    private void validateActiveUserStatus(User user) throws CustomException {
        if (!user.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new CustomException(ErrorCode.USER_STATUS_IS_NOT_ACTIVE);
        }
    }

    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    // 유저 검색
    public UserSearchResponseDto searchUsers(String keyword, Pageable pageable) {
        // 키워드의 이스케이프 처리
        String escapedWord = escapeSpecialCharacters(keyword);
        Page<User> userPage = getSearchedUserPage(escapedWord, pageable);

        Page<UserUnitDto> userUnitDtoPage = userPage.map(UserUnitDto::from);
        return UserSearchResponseDto.from(userUnitDtoPage);
    }

    @Transactional(readOnly = true)
    public Page<User> getSearchedUserPage(String keyword, Pageable pageable) {
        return userRepository.searchAllByNicknameContaining(keyword, pageable);
    }
}
