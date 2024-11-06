package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExcerptService {
    private final ExcerptRepository excerptRepository;
    private final UserService userService;
    private final UserBookService userBookService;

    public Excerpt createExcerpt(ExcerptCreateRequestDto requestDto){
        User user = userService.getCurrentUser();
        UserBook userBook = userBookService.findUserBookById(requestDto.userBookId());
        Excerpt excerpt = requestDto.toEntity(user, userBook, false);
        return excerptRepository.save(excerpt);
    }

    public Excerpt getExcerptById(Long excerptId) {
        return excerptRepository.findById(excerptId)
                .orElseThrow(() -> new CustomException(ErrorCode.EXCERPT_NOT_FOUND));
    }

}
