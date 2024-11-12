package com.mmc.bookduck.domain.book.service;

import com.mmc.bookduck.domain.archive.dto.request.ArchiveCreateRequestDto;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.dto.request.CustomBookRequestDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.CustomBookResponseDto;
import com.mmc.bookduck.domain.book.dto.response.UserBookResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.oneline.repository.OneLineRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBookService {

    private final BookInfoService bookInfoService;
    private final UserBookRepository userBookRepository;
    private final GenreService genreService;
    private final UserService userService;
    private final OneLineRepository oneLineRepository;

    //customBook 추가
    public UserBook createCustomBookEntity(CustomBookRequestDto requestDto) {
        User user = userService.getCurrentUser();
        BookInfo bookInfo = bookInfoService.saveCustomBookInfo(requestDto, user);
        UserBook userBook = new UserBook(ReadStatus.NOT_STARTED, user, bookInfo);
        return userBookRepository.save(userBook);
    }

    public CustomBookResponseDto createCustomBook(CustomBookRequestDto requestDto) {
        UserBook userBook = createCustomBookEntity(requestDto);
        return CustomBookResponseDto.from(userBook, null, null);
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

    // UserBook 정보 불러오거나 없으면 생성하기 (Archive에서 활용)
    @Transactional(readOnly = true)
    public UserBook getUserBookFromExcerptOrReview(Excerpt excerpt, Review review) {
        if (excerpt != null) {
            return excerpt.getUserBook();
        } else if (review != null) {
            return getUserBookById(review.getUserBook().getUserBookId());
        } else {
            throw new CustomException(ErrorCode.USERBOOK_NOT_FOUND);
        }
    }

    public UserBook getUserBookOrAdd(Excerpt excerpt, Review review, ArchiveCreateRequestDto requestDto) {
        // Excerpt와 Review에서 UserBook을 가져오려 시도
        try {
            return getUserBookFromExcerptOrReview(excerpt, review);
        } catch (CustomException e) {
            // CustomException이 발생하면 UserBook 새로 생성
            if (requestDto.getUserBook() != null) {
                return addUserBookEntity(requestDto.getUserBook());
            } else if (requestDto.getCustomBook() != null) {
                return createCustomBookEntity(requestDto.getCustomBook());
            } else {
                throw new CustomException(ErrorCode.USERBOOK_NOT_FOUND);
            }
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
    // 수정 필요
//    public UserBookListResponseDto getAllUserBook(String sort){
//
//        User user = userService.getCurrentUser();
//        List<UserBook> userBookList = sortUserBook(user, sort);
//
//        List<UserBookResponseDto> dtos = new ArrayList<>();
//        for(UserBook book : userBookList){
//                dtos.add(UserBookResponseDto.from(book));
//        }
//        return new UserBookListResponseDto(dtos);
//    }

    // 서재 책 상태별 조회
    // 수정 필요
//    public UserBookListResponseDto getStatusUserBook(List<String> statusList, String sort){
//
//        if(statusList.isEmpty()){
//            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
//        }
//
//        List<ReadStatus> readStatusList = new ArrayList<>();
//        try {
//            for(String status : statusList){
//                readStatusList.add(ReadStatus.valueOf(status));
//            }
//        } catch (IllegalArgumentException e) {
//            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
//        }
//
//        User user = userService.getCurrentUser();
//        List<UserBook> sorteduserBookList = sortUserBook(user, sort);
//
//        List<UserBook> userBookList = new ArrayList<>();
//        for(UserBook userBook : sorteduserBookList){
//            for(ReadStatus status : readStatusList){
//                if(userBook.getReadStatus().equals(status)){
//                    userBookList.add(userBook);
//                }
//            }
//        }
//
//        List<UserBookResponseDto> dtos = new ArrayList<>();
//        for(UserBook book : userBookList){
//            dtos.add(UserBookResponseDto.from(book));
//        }
//        return new UserBookListResponseDto(dtos);
//    }


    // 서재책 상세보기 - 기본정보
    // 수정필요
//    @Transactional(readOnly = true)
//    public BookInfoBasicResponseDto getUserBookInfoBasic(Long userBookId) {
//        UserBook userBook = findUserBookById(userBookId);
//
//        String koreanGenreName = genreService.genreNameToKorean(userBook.getBookInfo().getGenre());
//        BookInfoDetailDto detailDto = BookInfoDetailDto.from(userBook.getBookInfo(), koreanGenreName);
//
//        OneLine oneLine = oneLineRepository.findByUserBook(userBook)
//                .orElse(null);
//
//        double ratingAverage = getRatingAverage(findAllUserBookByBookInfo(userBook.getBookInfo()));
//
//        return new BookInfoBasicResponseDto(
//                ratingAverage,
//                oneLine !=null ? oneLine.getOneLineContent() : null,
//                oneLine !=null ? oneLine.getRating() : null,
//                userBook.getReadStatus(),
//                detailDto);
//    }

    /*
    // 서재 책 상세보기 - 추가정보
    @Transactional(readOnly = true)
    public BookInfoAdditionalResponseDto getUserBookInfoAdditional(Long userBookId) {
        UserBook userBook = findUserBookById(userBookId);
        List<UserBook> sameBookInfo_userBookList = findAllUserBookByBookInfo(userBook.getBookInfo());

        List<BookRatingUnitDto> oneLineList = new ArrayList<>();
        if (!sameBookInfo_userBookList.isEmpty()) {
            for (UserBook book : sameBookInfo_userBookList) {
                if (!book.equals(userBook)) {
                    oneLineRepository.findByUserBook(book).ifPresent(oneLineRating -> oneLineList.add(
                            BookRatingUnitDto.from(oneLineRating)));
                }
            }
        }
        return new BookInfoAdditionalResponseDto(oneLineList);
    }

     */


    // 수정 필요
//    @Transactional(readOnly = true)
//    public List<UserBook> sortUserBook(User user, String sort){
//
//        if(sort.equals("latest")){
//            return userBookRepository.findAllByUserOrderByCreatedTimeDesc(user);
//        }else if(sort.equals("rating")){
//            return userBookRepository.findByUserOrderByRating(user);
//        }else if(sort.equals("title")){
//            return userBookRepository.findAllByUserOrderByTitle(user);
//        }else{
//            throw new CustomException(ErrorCode.ERROR);
//        }
//    }

    @Transactional(readOnly = true)
    public List<UserBook> findAllUserBookByBookInfo(BookInfo bookInfo){
        return userBookRepository.findAllByBookInfo(bookInfo);
    }

    // 수정 필요
//    @Transactional(readOnly = true)
//    public double getRatingAverage(List<UserBook> userBookList) {
//
//        double totalRating = 0.0;
//        int count = 0;
//
//        for(UserBook book : userBookList){
//            Optional<OneLine> oneLineRating = oneLineRepository.findByUserBook(book);
//            if (oneLineRating.isPresent()) {
//                totalRating += oneLineRating.get().getRating();
//                count++;
//            }
//        }
//        return count > 0 ? totalRating / count : 0.0;
//    }

    @Transactional(readOnly = true)
    public List<UserBook> findAllByUser(User user) {
        return userBookRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public long countFinishedUserBooksByUser(User user) {
        return userBookRepository.countByUserAndReadStatus(user, ReadStatus.FINISHED);
    }

    @Transactional(readOnly = true)
    public void validateUserBookOwner(Long userBookId) {
        UserBook userBook = getUserBookById(userBookId);
        User currentUser = userService.getCurrentUser();
        if(!userBook.getUser().getUserId().equals(currentUser.getUserId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }
}