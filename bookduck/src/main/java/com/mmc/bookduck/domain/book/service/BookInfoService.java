package com.mmc.bookduck.domain.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.bookduck.domain.book.dto.common.BookRatingUnitDto;
import com.mmc.bookduck.domain.book.dto.common.CustomBookUnitResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoAdditionalResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookUnitResponseDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.BookInfoRepository;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.onelinerating.entity.OneLineRating;
import com.mmc.bookduck.domain.onelinerating.repository.OneLineRatingRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.google.GoogleBooksApiService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookInfoService {
    private final BookInfoRepository bookInfoRepository;
    private final UserBookRepository userBookRepository;
    private final GenreService genreService;
    private final GoogleBooksApiService googleBooksApiService;
    private final UserService userService;
    private final OneLineRatingRepository oneLineRatingRepository;


    // api 도서 목록 조회
    public BookListResponseDto<BookUnitResponseDto> searchBookList(String keyword, Long page, Long size) {
        String responseBody = googleBooksApiService.searchBookList(keyword, page, size);
        return parseBookInfo(responseBody);
    }

    // 목록 정보 파싱
    public BookListResponseDto<BookUnitResponseDto> parseBookInfo(String apiResult){
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(apiResult);
            JsonNode itemsNode = rootNode.path("items");

            List<BookUnitResponseDto> bookList = new ArrayList<>();

            for(JsonNode itemNode : itemsNode) {
                // providerId
                String providerId = itemNode.get("id").asText();

                JsonNode info = itemNode.get("volumeInfo");
                // title
                String title = getTextNode(info, "title");
                // authors
                JsonNode authorsNode = info.get("authors");
                List<String> authors = new ArrayList<>();
                if (authorsNode != null && authorsNode.isArray()) {
                    for (JsonNode authorNode : authorsNode) {
                        authors.add(authorNode.asText());
                    }
                } else {
                    // authors가 없으면, null로 설정
                    authors = null;
                }
                // image
                String imgPath;
                JsonNode imageLink = info.get("imageLinks");
                if (imageLink != null && imageLink.has("thumbnail")) {
                    imgPath = imageLink.get("thumbnail").asText();
                } else if (imageLink != null && imageLink.has("smallThumbnail")){
                    imgPath = imageLink.get("smallThumbnail").asText();
                } else {
                    imgPath = null;
                }
                bookList.add(new BookUnitResponseDto(title, authors, imgPath, providerId));
            }
            return new BookListResponseDto<>(bookList);

        }catch(Exception e){
            throw new CustomException(ErrorCode.JSON_PARSING_ERROR);
        }
    }

    private String getTextNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            return node.get(fieldName).asText();
        }
        return null; // 필드가 없을 경우 null
    }

    //api 도서 기본 정보 조회
    public BookInfoBasicResponseDto getOneBookBasic(String providerId) {
        String responseBody = googleBooksApiService.searchOneBook(providerId);
        BookInfoDetailDto additional = parseBookDetail(responseBody);

        Optional<BookInfo> bookInfo = bookInfoRepository.findByProviderId(providerId);
        if(bookInfo.isPresent()){
            Optional<UserBook> userBook = userBookRepository.findByUserAndBookInfo(userService.getCurrentUser(), bookInfo.get());
            if(userBook.isPresent()){
                Optional<OneLineRating> oneLineRating = oneLineRatingRepository.findByUserBook(userBook.get());
                if(oneLineRating.isPresent()){
                    return new BookInfoBasicResponseDto(getRatingAverage(bookInfo.get()), oneLineRating.get().getOneLineContent(),
                            oneLineRating.get().getRating(), userBook.get().getReadStatus(), additional);
                }else{
                    return new BookInfoBasicResponseDto(getRatingAverage(bookInfo.get()), null, null,
                            userBook.get().getReadStatus(), additional);
                }
            }else{
                return new BookInfoBasicResponseDto(getRatingAverage(bookInfo.get()), null, null, null, additional);
            }
        }
        else{
            return new BookInfoBasicResponseDto(null, null, null, null, additional);
        }
    }

    // 기본 정보 파싱
    private BookInfoDetailDto parseBookDetail(String responseBody) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode info = rootNode.get("volumeInfo");

            // publisher
            String publisher = getTextNode(info, "publisher");
            // publishedDate
            String publishedDate = getTextNode(info, "publishedDate");
            // description
            String description = getTextNode(info, "description");
            // page
            Long page = info.get("pageCount").asLong(0);

            // categories
            JsonNode cateNode = info.get("categories");
            List<String> cate = new ArrayList<>();
            if (cateNode != null && cateNode.isArray()) {
                for (JsonNode c : cateNode) {
                    cate.add(c.asText());
                }
            } else {
                //카테고리가 없으면, null로 설정
                cate = null;
            }
            //장르 매칭
            Genre genre = genreService.matchGenre(cate);
            String koreanGenre = genreService.genreNameToKorean(genre);

            String language = getTextNode(info, "language");
            return new BookInfoDetailDto(publisher, publishedDate, description, page, cate, genre.getGenreId(), koreanGenre, language);

        }catch(Exception e){
            throw new CustomException(ErrorCode.JSON_PARSING_ERROR);
        }
    }

    // api bookInfo 저장
    public BookInfo saveApiBookInfo (UserBookRequestDto dto) {

        String saveAuthor = dto.authors().getFirst();
        Genre genre = genreService.findGenreById(dto.genreId());

        BookInfo bookInfo = dto.toEntity(saveAuthor,genre);
        return bookInfoRepository.save(bookInfo);
    }

    @Transactional(readOnly = true)
    public Optional<BookInfo> findBookInfoByProviderId(String providerId) {
        return bookInfoRepository.findByProviderId(providerId);
    }

    // bookInfo 삭제
    public void deleteBookInfo(Long bookInfoId) {
        BookInfo bookInfo = bookInfoRepository.findById(bookInfoId)
                .orElseThrow(()-> new CustomException(ErrorCode.BOOKINFO_NOT_FOUND));
        bookInfoRepository.delete(bookInfo);
    }

    // custom book 목록 검색
    @Transactional(readOnly = true)
    public BookListResponseDto<CustomBookUnitResponseDto> searchCustomBookList(String keyword, Long page, Long size) {

        User user = userService.getCurrentUser();
        List<BookInfo> bookInfoList = bookInfoRepository.searchByCreatedUserIdAndKeyword(user.getUserId(), keyword);

        List<CustomBookUnitResponseDto> dtos = new ArrayList<>();
        for(BookInfo bookInfo : bookInfoList){
            dtos.add(CustomBookUnitResponseDto.from(bookInfo));
        }
        return new BookListResponseDto<>(dtos);
    }


    //api도서 상세정보 - 추가정보
    @Transactional(readOnly = true)
    public BookInfoAdditionalResponseDto getApiBookAdditional(String providerId) {
        User user = userService.getCurrentUser();
        BookInfo bookInfo = bookInfoRepository.findByProviderId(providerId)
                .orElseThrow(()-> new CustomException(ErrorCode.BOOKINFO_NOT_FOUND));
        List<UserBook> sameBookInfo_userBookList = userBookRepository.findAllByBookInfo(bookInfo);

        List<BookRatingUnitDto> oneLineList = new ArrayList<>();
        if (!sameBookInfo_userBookList.isEmpty()) {
            for (UserBook book : sameBookInfo_userBookList) {
                if (!book.getUser().equals(user)) {
                    oneLineRatingRepository.findByUserBook(book).ifPresent(oneLineRating -> oneLineList.add(
                            BookRatingUnitDto.from(oneLineRating)));
                }
            }
        }
        return new BookInfoAdditionalResponseDto(oneLineList);
    }

    // custom 기본정보
    @Transactional(readOnly = true)
    public BookInfoBasicResponseDto getCustomBookBasic(Long bookInfoId) {
        User user = userService.getCurrentUser();
        BookInfo bookInfo = bookInfoRepository.findById(bookInfoId)
                .orElseThrow(()-> new CustomException(ErrorCode.BOOKINFO_NOT_FOUND));

        UserBook userBook = userBookRepository.findByUserAndBookInfo(user, bookInfo)
                .orElseThrow(()-> new CustomException(ErrorCode.USERBOOK_NOT_FOUND));

        String koreanGenreName = genreService.genreNameToKorean(userBook.getBookInfo().getGenre());
        BookInfoDetailDto detailDto = BookInfoDetailDto.from(userBook, koreanGenreName);

        OneLineRating oneLineRating = oneLineRatingRepository.findByUserBook(userBook)
                .orElse(null);

        return new BookInfoBasicResponseDto(
                null,
                oneLineRating!=null ? oneLineRating.getOneLineContent() : null,
                oneLineRating!=null ? oneLineRating.getRating() : null,
                userBook.getReadStatus(),
                detailDto);
    }

    @Transactional(readOnly = true)
    public Double getRatingAverage(BookInfo bookInfo) {
        double totalRating = 0.0;
        int count = 0;

        List<UserBook> userBookList = userBookRepository.findAllByBookInfo(bookInfo);
        if(userBookList.isEmpty()){
            // 별점 아예 없는 경우에는 NULL
            return null;
        }

        for(UserBook book : userBookList){
            Optional<OneLineRating> oneLineRating = oneLineRatingRepository.findByUserBook(book);
            if (oneLineRating.isPresent()) {
                totalRating += oneLineRating.get().getRating();
                count++;
            }
        }
        return count > 0 ? totalRating / count : 0.0;
    }
}
