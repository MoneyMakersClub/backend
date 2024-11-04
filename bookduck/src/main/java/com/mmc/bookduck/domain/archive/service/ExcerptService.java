package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.response.ExcerptResponseDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ExcerptService {
    private final ExcerptRepository excerptRepository;
    private final UserService userService;
    private final UserBookService userBookService;

    public Excerpt createExcerpt(ExcerptCreateRequestDto requestDto, LocalDateTime createdTime){
        User user = userService.getCurrentUser();
        UserBook userBook = userBookService.findUserBookById(requestDto.userBookId());
        boolean isMain = requestDto.isMain() != null ? requestDto.isMain() : false;
        Excerpt excerpt = requestDto.toEntity(user, userBook, isMain);
        excerpt.setCreatedTime(createdTime);
        return excerptRepository.save(excerpt);
    }
}
