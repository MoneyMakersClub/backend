package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExcerptService {
    private final ExcerptRepository excerptRepository;
    private final UserService userService;
    private final UserBookService userBookService;

    public Excerpt createExcerpt(ExcerptCreateRequestDto requestDto){
        log.debug("createExcerpt 접근 시도 : ", requestDto);
        User user = userService.getCurrentUser();
        UserBook userBook = userBookService.findUserBookById(requestDto.getUserBookId());
        log.debug("userBookId : ", userBook);
        Visibility visibility = requestDto.getVisibility() != null ? requestDto.getVisibility() : Visibility.PUBLIC;
        Excerpt excerpt = requestDto.toEntity(user, userBook, false, visibility);
        log.debug("excerpt : ", excerpt);
        return excerptRepository.save(excerpt);
    }

    @Transactional(readOnly = true)
    public Excerpt getExcerptById(Long excerptId) {
        return excerptRepository.findById(excerptId)
                .orElseThrow(() -> new CustomException(ErrorCode.EXCERPT_NOT_FOUND));
    }

}
