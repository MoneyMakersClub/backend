package com.mmc.bookduck.domain.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.bookduck.domain.book.dto.response.BookUnitResponseDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.dto.response.BookInfoBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.BookInfoRepository;
import com.mmc.bookduck.domain.book.repository.GenreRepository;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.repository.UserRepository;
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


    // api 도서 목록 조회
    public BookListResponseDto searchBookList(String keyword, Long page, Long size) {
        String responseBody = googleBooksApiService.searchBookList(keyword, page, size);
        return parseBookInfo(responseBody);
    }

    // 목록 정보 파싱
    public BookListResponseDto parseBookInfo(String apiResult){
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
            return new BookListResponseDto(bookList);

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

    // 임시 User
    private final UserRepository userRepository;

    public User findUser(){
        User user = userRepository.findById(1L)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
    //


    //api 도서 기본 정보 조회
    public BookInfoBasicResponseDto getOneBookBasic(String providerId) {
        String responseBody = googleBooksApiService.searchOneBook(providerId);
        BookInfoDetailDto additional = parseBookDetail(responseBody);

        Double ratingAverage;
        ReadStatus readStatus;
        String myOneLine;
        Double myRating;

        Optional<BookInfo> bookInfo = bookInfoRepository.findByProviderId(providerId);
        if(bookInfo.isPresent()){
            // ratingAverage = getBookRating(bookInfo); -> 별점, 한줄평 응답 추후 수정
            Optional<UserBook> userBook = userBookRepository.findByUserAndBookInfo(findUser(), bookInfo.get());
            if(userBook.isPresent()){
                // 별점 한줄평 개발 후 추후 수정
                return new BookInfoBasicResponseDto(null, null, null, null, additional);
            }else{
                return new BookInfoBasicResponseDto(null, null, null, null, additional);
            }
        }
        else{
            return new BookInfoBasicResponseDto(null, null, null, null, additional);
        }
    }

    /*
    // 책 별 rating
    public Double getBookRating(BookInfo bookInfo){
        List<UserBook> books = userBookRepository.findByBookInfo(bookInfo);
        for(UserBook userBook : books){
            if()
        }
    }
    */


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

    /*
    // 직접 등록한 책 검색
    public BookListResponseDto searchCustomBookList(String keyword, Long page, Long size) {

        // 먼저 user 검색
        User user = userRepository.findById(userId);

        List<BookInfo> bookInfos = bookInfoRepository.searchByCreatedUserIdAndKeyword(user.getUserId(), keyword);
        if(bookInfos != null){

            List<BookInfoUnitDto> bookList = new ArrayList<>();

            for(BookInfo bookinfo = bookInfos){
                Long publishedYear = extractYear(bookinfo.getPublishDate());
                // 저자를 리스트로 변환
                List<String> authors = Collections.singletonList(bookinfo.getAuthor());
                bookList.add(new BookInfoUnitDto(bookinfo.getTitle(), authors, bookinfo.getPublisher(), publishedYear, bookinfo.getImgPath(), bookinfo.getProviderId()));
            }
            return new BookListResponseDto(bookList);
        }else{
            // 검색결과 없는 경우
            return new BookListResponseDto(null);
        }
    }
    */
    
}
