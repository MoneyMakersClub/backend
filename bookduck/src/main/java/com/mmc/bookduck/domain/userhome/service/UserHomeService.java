package com.mmc.bookduck.domain.userhome.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.userhome.dto.common.*;
import com.mmc.bookduck.domain.userhome.dto.request.ReadingSpaceUpdateRequestDto;
import com.mmc.bookduck.domain.userhome.dto.request.HomeCardRequestDto;
import com.mmc.bookduck.domain.userhome.dto.response.ReadingSpaceResponseDto;
import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import com.mmc.bookduck.domain.userhome.repository.UserHomeRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHomeService {
    private final UserService userService;
    private final UserHomeRepository userHomeRepository;
    private final HomeCardService homeCardService;

    @Transactional(readOnly = true)
    public UserHome getUserHomeOfUser(User user) {
        return userHomeRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USERHOME_NOT_FOUND));
    }

    public void deleteHomeCardsByExcerpt(Excerpt excerpt) {
        UserHome userHome = getUserHomeOfUser(excerpt.getUser());
        homeCardService.deleteHomeCardsByExcerpt(userHome, excerpt);
    }

    public void deleteHomeCardsByOneLine(OneLine oneLine) {
        UserHome userHome = getUserHomeOfUser(oneLine.getUser());
        homeCardService.deleteHomeCardsByOneLine(userHome, oneLine);
    }

    public void deleteHomeCardsByUserBook(UserBook userBook) {
        UserHome userHome = getUserHomeOfUser(userBook.getUser());
        homeCardService.deleteHomeCardsByUserBook(userHome, userBook);
    }
}
