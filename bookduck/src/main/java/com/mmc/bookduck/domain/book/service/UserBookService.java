package com.mmc.bookduck.domain.book.service;

import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.dto.common.BookRatingUnitDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoAdditionalResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookListResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.onelinerating.entity.OneLineRating;
import com.mmc.bookduck.domain.onelinerating.repository.OneLineRatingRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBookService {

    private final BookInfoService bookInfoService;
    private final UserBookRepository userBookRepository;
    private final GenreService genreService;
    private final UserService userService;
    private final OneLineRatingRepository oneLineRatingRepository;


    // 서재에 책 추가
    public UserBookResponseDto addUserBook(UserBookRequestDto dto) {
        try {
            ReadStatus.valueOf(dto.readStatus());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }

        Optional<BookInfo> bookInfo = bookInfoService.findBookInfoByProviderId(dto.providerId());

        if(bookInfo.isPresent()){
            Optional<UserBook> userBook = userBookRepository.findByUserAndBookInfo(userService.getCurrentUser(), bookInfo.get());

            if(userBook.isPresent()){
                throw new CustomException(ErrorCode.USERBOOK_ALREADY_EXISTS);
            }
            UserBook newUserBook = dto.toEntity(userService.getCurrentUser(), bookInfo.get(), ReadStatus.valueOf(dto.readStatus()));
            UserBook savedUserBook = userBookRepository.save(newUserBook);
            return UserBookResponseDto.from(savedUserBook);
        }
        else{
            // bookInfo 없으면 먼저 bookInfo 저장
            BookInfo newBookInfo = bookInfoService.saveApiBookInfo(dto);

            UserBook newUserBook = dto.toEntity(userService.getCurrentUser(),newBookInfo, ReadStatus.valueOf(dto.readStatus()));
            UserBook savedUserBook = userBookRepository.save(newUserBook);
            return UserBookResponseDto.from(savedUserBook);
        }
    }

    // 서재에서 책 삭제
    public String deleteUserBook(Long userBookId) {
        UserBook userBook = findUserBookById(userBookId);

        //임시 User
        Long userId = userService.getCurrentUser().getUserId();

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
    public UserBookResponseDto updateUserBookStatus(Long userBookId, String status) {
        try {
            ReadStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }

        UserBook userBook = findUserBookById(userBookId);

        //임시 User
        Long userId = userService.getCurrentUser().getUserId();

        // 권한체크
        if(userBook.getUser().getUserId().equals(userId)){
            userBook.changeReadStatus(ReadStatus.valueOf(status));

            return UserBookResponseDto.from(userBook);
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public UserBook findUserBookById(Long userBookId){
        UserBook userBook = userBookRepository.findById(userBookId)
                .orElseThrow(()-> new CustomException(ErrorCode.USERBOOK_NOT_FOUND));
        return userBook;
    }

    // 서재 책 전체 조회
    public UserBookListResponseDto getAllUserBook(String sort){

        User user = userService.getCurrentUser();
        List<UserBook> userBookList = sortUserBook(user, sort);

        List<UserBookResponseDto> dtos = new ArrayList<>();
        for(UserBook book : userBookList){
                dtos.add(UserBookResponseDto.from(book));
        }
        return new UserBookListResponseDto(dtos);
    }

    // 서재 책 상태별 조회
    public UserBookListResponseDto getStatusUserBook(List<String> statusList, String sort){

        if(statusList.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }

        List<ReadStatus> readStatusList = new ArrayList<>();
        try {
            for(String status : statusList){
                readStatusList.add(ReadStatus.valueOf(status));
            }
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }

        User user = userService.getCurrentUser();
        List<UserBook> sorteduserBookList = sortUserBook(user, sort);

        List<UserBook> userBookList = new ArrayList<>();
        for(UserBook userBook : sorteduserBookList){
            for(ReadStatus status : readStatusList){
                if(userBook.getReadStatus().equals(status)){
                    userBookList.add(userBook);
                }
            }
        }

        List<UserBookResponseDto> dtos = new ArrayList<>();
        for(UserBook book : userBookList){
            dtos.add(UserBookResponseDto.from(book));
        }
        return new UserBookListResponseDto(dtos);
    }


    // 서재책 상세보기 - 기본정보
    @Transactional(readOnly = true)
    public BookInfoBasicResponseDto getUserBookInfoBasic(Long userBookId) {
        UserBook userBook = findUserBookById(userBookId);

        String koreanGenreName = genreService.genreNameToKorean(userBook.getBookInfo().getGenre());
        BookInfoDetailDto detailDto = BookInfoDetailDto.from(userBook, koreanGenreName);

        OneLineRating oneLineRating = oneLineRatingRepository.findByUserBook(userBook)
                .orElse(null);

        // 추후 수정
        return new BookInfoBasicResponseDto(
                null,
                oneLineRating!=null ? oneLineRating.getOneLineContent() : null,
                oneLineRating!=null ? oneLineRating.getRating() : null,
                userBook.getReadStatus(),
                detailDto);
    }

    // 서재 책 상세보기 - 추가정보
    @Transactional(readOnly = true)
    public BookInfoAdditionalResponseDto getUserBookInfoAdditional(Long userBookId) {
        UserBook userBook = findUserBookById(userBookId);
        List<UserBook> sameBookInfo_userBookList = userBookRepository.findAllByBookInfo(userBook.getBookInfo());

        List<BookRatingUnitDto> oneLineList = new ArrayList<>();
        if (!sameBookInfo_userBookList.isEmpty()) {
            for (UserBook book : sameBookInfo_userBookList) {
                if (!book.equals(userBook)) {
                    oneLineRatingRepository.findByUserBook(book).ifPresent(oneLineRating -> oneLineList.add(
                            BookRatingUnitDto.from(oneLineRating)));
                }
            }
        }
        return new BookInfoAdditionalResponseDto(oneLineList);
    }


    @Transactional(readOnly = true)
    public List<UserBook> sortUserBook(User user, String sort){

        if(sort.equals("latest")){
            return userBookRepository.findAllByUserOrderByCreatedTimeDesc(user);
        }else if(sort.equals("rating")){
            return userBookRepository.findByUserOrderByRating(user);
        }else if(sort.equals("title")){
            return userBookRepository.findAllByUserOrderByTitle(user);
        }else{
            throw new CustomException(ErrorCode.ERROR);
        }
    }
}