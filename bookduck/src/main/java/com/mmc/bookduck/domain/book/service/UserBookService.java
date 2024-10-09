package com.mmc.bookduck.domain.book.service;

import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBookService {

    private final BookInfoService bookInfoService;
    private final UserBookRepository userBookRepository;


    // 임시 User
    private final UserRepository userRepository;

    public User findUser(){
        User user = userRepository.findById(1L)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
    //


    // 서재에 책 추가
    @Transactional
    public UserBookResponseDto addUserBook(UserBookRequestDto dto, String status) {

        BookInfo bookInfo = bookInfoService.findBookInfoByProviderId(dto.getProviderId());
        if(bookInfo == null) {
            // 없으면 먼저 bookInfo 저장
            bookInfo = bookInfoService.saveApiBookInfo(dto);
        }

        UserBook userBook = UserBook.builder()
                .user(findUser())
                .readStatus(ReadStatus.valueOf(status))
                .bookInfo(bookInfo)
                .build();

        UserBook savedUserBook = userBookRepository.save(userBook);

        return new UserBookResponseDto(savedUserBook.getUserBookId(), savedUserBook.getBookInfo().getTitle(), savedUserBook.getBookInfo().getAuthor(),
                savedUserBook.getBookInfo().getImgPath(), savedUserBook.getReadStatus(), savedUserBook.getBookInfo().getBookInfoId());
    }

    // 서재에서 책 삭제
    @Transactional
    public String deleteUserBook(Long userBookId) {

        UserBook userBook = userBookRepository.findById(userBookId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_BOOK_NOT_FOUND));

        //임시 User
        Long userId = findUser().getUserId();

        // 권한체크
        if(userBook.getUser().getUserId().equals(userId)){

            BookInfo bookInfo = userBook.getBookInfo();
            Long createdUserId = bookInfo.getCreatedUserId();
            // 사용자가 직접 등록한 책이면 bookInfo도 같이 삭제
            if(createdUserId != null && createdUserId.equals(userId)){
                bookInfoService.deleteBookInfo(bookInfo.getBookInfoId());
            }
            userBookRepository.delete(userBook);
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        return "서재에서 책이 삭제되었습니다.";
    }

    // 서재 책 상태 변경
    @Transactional
    public UserBookResponseDto updateUserBookStatus(Long userBookId, String status) {
        UserBook userBook = userBookRepository.findById(userBookId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_BOOK_NOT_FOUND));

        //임시 User
        Long userId = findUser().getUserId();

        // 권한체크
        if(userBook.getUser().getUserId() == userId){
            userBook.changeReadStatus(ReadStatus.valueOf(status));

            return new UserBookResponseDto(userBook.getUserBookId(), userBook.getBookInfo().getTitle(), userBook.getBookInfo().getAuthor(),
                    userBook.getBookInfo().getImgPath(),userBook.getReadStatus(), userBook.getBookInfo().getBookInfoId());
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }
}
