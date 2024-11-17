package com.mmc.bookduck.domain.book.service;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ExcerptCreateRequestDto;
import com.mmc.bookduck.domain.archive.dto.request.ReviewCreateRequestDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.book.dto.common.BookCoverImageUnitDto;
import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.dto.common.BookRatingUnitDto;
import com.mmc.bookduck.domain.book.dto.request.CustomBookRequestDto;
import com.mmc.bookduck.domain.book.dto.request.RatingRequestDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.*;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.oneline.repository.OneLineRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.homecard.service.HomeCardService;
import com.mmc.bookduck.global.common.BaseTimeEntity;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserBookService {
    private final BookInfoService bookInfoService;
    private final UserBookRepository userBookRepository;
    private final GenreService genreService;
    private final UserService userService;
    private final OneLineRepository oneLineRepository;
    private final ExcerptRepository excerptRepository;
    private final ReviewRepository reviewRepository;
    private final HomeCardService homeCardService;

    //customBook 추가
    public UserBook createCustomBookEntity(CustomBookRequestDto requestDto) {
        User user = userService.getCurrentUser();
        BookInfo bookInfo = bookInfoService.saveCustomBookInfo(requestDto, user);
        UserBook userBook = new UserBook(ReadStatus.NOT_STARTED, user, bookInfo);
        return userBookRepository.save(userBook);
    }

    public CustomBookResponseDto createCustomBook(CustomBookRequestDto requestDto) {
        UserBook userBook = createCustomBookEntity(requestDto);
        return CustomBookResponseDto.from(userBook, 0.0, null, true);
    }

    // 서재에 책 추가
    public UserBookResponseDto addUserBook(UserBookRequestDto requestDto) {
        UserBook savedUserBook = addUserBookEntity(requestDto);
        return UserBookResponseDto.from(savedUserBook);
    }

    public UserBook addUserBookEntity(UserBookRequestDto requestDto) {
        try {
            ReadStatus.valueOf(requestDto.readStatus());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }

        Optional<BookInfo> bookInfo = bookInfoService.findBookInfoByProviderId(requestDto.providerId());

        if(bookInfo.isPresent()){
            Optional<UserBook> userBook = userBookRepository.findByUserAndBookInfo(userService.getCurrentUser(), bookInfo.get());

            if(userBook.isPresent()){
                throw new CustomException(ErrorCode.USERBOOK_ALREADY_EXISTS);
            }
            UserBook newUserBook = requestDto.toEntity(userService.getCurrentUser(), bookInfo.get(), ReadStatus.valueOf(requestDto.readStatus()));
            return userBookRepository.save(newUserBook);
        }
        else{
            // bookInfo 없으면 먼저 bookInfo 저장
            BookInfo newBookInfo = bookInfoService.saveApiBookInfo(requestDto);
            UserBook newUserBook = requestDto.toEntity(userService.getCurrentUser(),newBookInfo, ReadStatus.valueOf(requestDto.readStatus()));
            return userBookRepository.save(newUserBook);
        }
    }

    public UserBook getUserBookOrAdd(ExcerptCreateRequestDto excerptDto, ReviewCreateRequestDto reviewDto, ArchiveCreateRequestDto archiveDto) {
        // ExcerptDto 또는 ReviewDto에서 userBookId를 확인
        Long userBookId = (excerptDto != null && excerptDto.getUserBookId() != null)
                ? excerptDto.getUserBookId()
                : (reviewDto != null ? reviewDto.getUserBookId() : null);
        if (userBookId != null) {
            return getUserBookById(userBookId);
        }
        if (archiveDto.getUserBook() != null) {
            return addUserBookEntity(archiveDto.getUserBook());
        } else if (archiveDto.getCustomBook() != null) {
            return createCustomBookEntity(archiveDto.getCustomBook());
        } else {
            throw new CustomException(ErrorCode.USERBOOK_NOT_FOUND);
        }
    }

    // 서재에서 책 삭제
    public String deleteUserBook(Long userBookId) {
        UserBook userBook = getUserBookById(userBookId);
        User user = userService.getCurrentUser();

        // 권한체크
        if(userBook.getUser().getUserId().equals(user.getUserId())){

            BookInfo bookInfo = userBook.getBookInfo();
            Long createdUserId = bookInfo.getCreatedUserId();

            userBookRepository.delete(userBook);
            // 사용자가 직접 등록한 책이면 bookInfo도 같이 삭제
            if(createdUserId != null && createdUserId.equals(user.getUserId())){
                bookInfoService.deleteCustomBookInfo(bookInfo.getBookInfoId());
            }
            homeCardService.deleteHomeCardsByUserBook(userBook);
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

        UserBook userBook = getUserBookById(userBookId);

        User user = userService.getCurrentUser();

        // 권한체크
        if(userBook.getUser().getUserId().equals(user.getUserId())){
            userBook.changeReadStatus(ReadStatus.valueOf(status));

            return UserBookResponseDto.from(userBook);
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public UserBook getUserBookById(Long userBookId){
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
        List<ReadStatus> readStatusList = validateReadStatus(statusList);

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
        UserBook userBook = getUserBookById(userBookId);

        String koreanGenreName = genreService.genreNameToKorean(userBook.getBookInfo().getGenre());
        BookInfoDetailDto detailDto = BookInfoDetailDto.from(userBook.getBookInfo(), koreanGenreName);

        OneLine oneLine = oneLineRepository.findByUserBook(userBook)
                .orElse(null);
        Double ratingAverage = bookInfoService.getRatingAverage(userBook.getBookInfo());

        if(oneLine != null){
            return BookInfoBasicResponseDto.from(userBook, ratingAverage, oneLine.getOneLineContent(), detailDto);
        }
        else{
            return BookInfoBasicResponseDto.from(userBook, ratingAverage, null, detailDto);
        }
    }


    // 서재 책 상세보기 - 추가정보
    @Transactional(readOnly = true)
    public BookInfoAdditionalResponseDto getUserBookInfoAdditional(Long userBookId) {
        UserBook userBook = getUserBookById(userBookId);
        List<UserBook> sameBookInfo_userBookList = findAllUserBookByBookInfo(userBook.getBookInfo());

        List<BookRatingUnitDto> oneLineList = new ArrayList<>();
        if (!sameBookInfo_userBookList.isEmpty()) {
            for (UserBook book : sameBookInfo_userBookList) {
                //내 userBook 제외
                if (!book.equals(userBook)) {
                    Optional<OneLine> oneLine =  oneLineRepository.findByUserBook(book);
                    if(oneLine.isPresent()){
                        oneLineList.add(BookRatingUnitDto.from(oneLine.get(), book));
                    }
                    if(oneLineList.size() == 3){
                        break;
                    }
                }
            }
        }
        return new BookInfoAdditionalResponseDto(oneLineList);
    }


    @Transactional(readOnly = true)
    public List<UserBook> sortUserBook(User user, String sort){

        if(sort.equals("latest")){
            return userBookRepository.findAllByUserOrderByCreatedTimeDesc(user);
        }else if(sort.equals("rating_high")){
            return userBookRepository.findByUserOrderByRatingDesc(user);
        }else if(sort.equals("rating_low")){
            return userBookRepository.findByUserOrderByRatingAsc(user);
        }else if(sort.equals("title")){
            return userBookRepository.findAllByUserOrderByTitle(user);
        }else{
            throw new CustomException(ErrorCode.ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<UserBook> findAllUserBookByBookInfo(BookInfo bookInfo){
        return userBookRepository.findAllByBookInfo(bookInfo);
    }

    @Transactional(readOnly = true)
    public List<UserBook> findAllByUser(User user) {
        return userBookRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public long countFinishedUserBooksByUser(User user) {
        return userBookRepository.countByUserAndReadStatus(user, ReadStatus.FINISHED);
    }

    @Transactional
    public RatingResponseDto ratingUserBook(Long userbookId, RatingRequestDto dto) {
        if ((dto.rating() % 0.5) != 0.0){
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        UserBook userBook = getUserBookById(userbookId);
        userBook.changeRating(dto.rating());

        return RatingResponseDto.from(userBook);
    }

    @Transactional
    public RatingResponseDto deleteRating(Long userbookId) {
        UserBook userBook = getUserBookById(userbookId);
        userBook.changeRating(0.0);

        return RatingResponseDto.from(userBook);
    }

    @Transactional(readOnly = true)
    public BookListResponseDto<BookCoverImageUnitDto> getRecentRecordBooks() {
        User user = userService.getCurrentUser();
        LocalDateTime monthsAgo = LocalDateTime.now().minusMonths(3);

        // 세 달 이내
        List<Excerpt> excerpts = excerptRepository.findAllByUserAndCreatedTimeAfter(user, monthsAgo);
        List<Review> reviews = reviewRepository.findAllByUserAndCreatedTimeAfter(user, monthsAgo);

        List<BaseTimeEntity> allItems = new ArrayList<>();
        allItems.addAll(excerpts);
        allItems.addAll(reviews);

        allItems.sort((item1, item2) -> item2.getCreatedTime().compareTo(item1.getCreatedTime()));

        List<UserBook> userBookList = new ArrayList<>();
        for (Object item : allItems) {
            if (userBookList.size() > 3 || userBookList.size() == 3) break;

            if (item instanceof Excerpt excerpt) {
                UserBook userBook = excerpt.getUserBook();
                if (!userBookList.contains(userBook)) {
                    userBookList.add(userBook);
                }
            }
            else if (item instanceof Review review) {
                UserBook userBook = review.getUserBook();
                if (!userBookList.contains(userBook)) {
                    userBookList.add(userBook);
                }
            }
        }
        List<BookCoverImageUnitDto> coverList = new ArrayList<>();
        for(UserBook userBook : userBookList){
            coverList.add(BookCoverImageUnitDto.from(userBook));
        }
        return new BookListResponseDto<>(coverList);
    }

    @Transactional(readOnly = true)
    public void validateUserBookOwner(UserBook userBook) {
        User currentUser = userService.getCurrentUser();
        if(!userBook.getUser().getUserId().equals(currentUser.getUserId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }

    public List<ReadStatus> validateReadStatus(List<String> statusList){
        List<ReadStatus> readStatusList = new ArrayList<>();
        try {
            for(String status : statusList){
                readStatusList.add(ReadStatus.valueOf(status));
            }
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
        return readStatusList;
    }
}