package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserStatus;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
