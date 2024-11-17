package com.mmc.bookduck.domain.archive.service;

import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ExcerptUpdateRequestDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.userhome.dto.common.ExcerptWithBookInfoUnitDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.common.PaginatedResponseDto;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mmc.bookduck.global.common.EscapeSpecialCharactersService.escapeSpecialCharacters;

@Service
@RequiredArgsConstructor
@Transactional
public class ExcerptService {
    private final ExcerptRepository excerptRepository;
    private final UserService userService;
    private final UserBookService userBookService;

    // 생성
    public Excerpt createExcerpt(ExcerptCreateRequestDto requestDto){
        User user = userService.getCurrentUser();
        UserBook userBook = userBookService.getUserBookById(requestDto.getUserBookId());
        Visibility visibility = requestDto.getVisibility() != null ? requestDto.getVisibility() : Visibility.PUBLIC;
        Excerpt excerpt = requestDto.toEntity(user, userBook, visibility);
        return excerptRepository.save(excerpt);
    }

    // 수정
    public Excerpt updateExcerpt(Long excerptId, ExcerptUpdateRequestDto requestDto) {
        // 생성자 검증 archiveService.updateArchive에서 하고 있으므로 생략
        Excerpt excerpt = getExcerptById(excerptId);
        excerpt.updateExcerpt(requestDto.excerptContent(), requestDto.pageNumber(), requestDto.excerptVisibility());
        return excerpt;
    }

    // 삭제
    public void deleteExcerpt(Long excerptId) {
        // 생성자 검증 archiveService.deleteArchive에서 하고 있으므로 생략
        Excerpt excerpt = getExcerptById(excerptId);
        excerptRepository.delete(excerpt);
    }

    @Transactional(readOnly = true)
    public Excerpt getExcerptById(Long excerptId) {
        return excerptRepository.findById(excerptId)
                .orElseThrow(() -> new CustomException(ErrorCode.EXCERPT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDto<ExcerptWithBookInfoUnitDto> searchExcerptsFromReadingSpace(String keyword, Pageable pageable) {
        User user = userService.getCurrentUser();
        String escapedWord = escapeSpecialCharacters(keyword);
        Page<Excerpt> excerptPage = excerptRepository.searchAllByExcerptContentOrBookInfoTitleOrAuthorByUserAndCreatedTimeDesc(escapedWord, user, pageable);
        Page<ExcerptWithBookInfoUnitDto> excerptDtoPage = excerptPage.map(ExcerptWithBookInfoUnitDto::new);
        return PaginatedResponseDto.from(excerptDtoPage);
    }
}
