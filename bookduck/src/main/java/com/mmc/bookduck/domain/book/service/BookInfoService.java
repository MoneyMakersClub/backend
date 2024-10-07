package com.mmc.bookduck.domain.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.bookduck.domain.book.dto.common.BookInfoUnitDto;
import com.mmc.bookduck.domain.book.dto.request.UserBookRequestDto;
import com.mmc.bookduck.domain.book.dto.response.ApiBookBasicResponseDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.repository.BookInfoRepository;
import com.mmc.bookduck.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookInfoService {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${google.books.api.key}")
    private String apiKey;

    private final BookInfoRepository bookInfoRepository;


    // api 도서 목록 조회
    public BookListResponseDto searchBookList(String keyword, Long page, Long size) {

        /*
        // 한글 검색어 입력하면 잘 안되는 문제
        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/volumes")
                .queryParam("q", keyword)
                .queryParam("startIndex", (page-1))
                .queryParam("maxResults", size)
                .queryParam("key", apiKey)
                .toUriString();
         */

        // 페이지 1부터 시작한다고 가정
        String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:"+keyword+"+OR+inauthor:"+keyword+"&startIndex="+(page-1)+"&maxResults="+size+"&key="+apiKey;

        // API GET 요청
        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        String responseBody = apiResponse.getBody();
        return parseBookInfo(responseBody);
    }

    // 목록 정보 파싱
    public BookListResponseDto parseBookInfo(String apiResult){

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            // 검색결과 없을 때는 null 보내는 코드 추가 필요
            JsonNode rootNode = objectMapper.readTree(apiResult);
            JsonNode itemsNode = rootNode.path("items");

            List<BookInfoUnitDto> bookList = new ArrayList<>();

            for(JsonNode itemNode : itemsNode) {
                // providerId
                String providerId = itemNode.get("id").asText();

                JsonNode info = itemNode.get("volumeInfo");

                // title
                String title = getTextNode(info, "title");
                // subtitle
                String subtitle = getTextNode(info, "subtitle");
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
                // publisher
                String publisher = getTextNode(info, "publisher");
                // publishedYear
                String publishedDate = getTextNode(info, "publishedDate");
                Long publishedYear = extractYear(publishedDate);

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
                bookList.add(new BookInfoUnitDto(title,subtitle, authors, publisher, publishedYear, imgPath, providerId));
            }
                return new BookListResponseDto(bookList);

        }catch(Exception e){
            System.err.println("Error parsing book info: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Long extractYear(String publishedDate) {
        if (publishedDate == null || publishedDate.isEmpty()) {
            return null;
        }
        else {
            return Long.parseLong(publishedDate.substring(0, 4));  //네자리 연도로 추출
        }
    }

    private String getTextNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            return node.get(fieldName).asText();
        }
        return null; // 필드가 없을 경우 null
    }

    //api 도서 기본 정보 조회
    public ApiBookBasicResponseDto getApiBookBasic(String providerId) {

        // 페이지 1부터 시작한다고 가정
        String url = "https://www.googleapis.com/books/v1/volumes/"+providerId+"?key="+apiKey;

        // API GET 요청
        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        String responseBody = apiResponse.getBody();
        return parseBookDetail(responseBody);
    }

    // 기본 정보 파싱
    private ApiBookBasicResponseDto parseBookDetail(String responseBody) {

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode info = rootNode.get("volumeInfo");

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
            return new ApiBookBasicResponseDto(description, page, cate);

        }catch(Exception e){
            System.err.println("Error parsing book Detail info: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // api bookInfo 저장
    @Transactional
    public BookInfo saveApiBookInfo (UserBookRequestDto dto) {
        List<String> authors = dto.getAuthors();

        String saveAuthor = authors.getFirst();

        // 페이지 1부터 시작한다고 가정
        String url = "https://www.googleapis.com/books/v1/volumes/"+dto.getProviderId()+"?key="+apiKey;

        // API GET 요청
        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        String responseBody = apiResponse.getBody();
        String language = parseLanguage(responseBody);

        BookInfo bookInfo = BookInfo.builder()
                .providerId(dto.getProviderId())
                .title(dto.getTitle())
                .author(saveAuthor)
                .publisher(dto.getPublisher())
                .publishDate(dto.getPublishDate())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .pageCount(dto.getPageCount())
                .imgPath(dto.getImgPath())
                .language(language)
                .build();

        return bookInfoRepository.save(bookInfo);
    }

    // language 파싱
    private String parseLanguage(String responseBody) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode info = rootNode.get("volumeInfo");

           return getTextNode(info, "language");

        }catch(Exception e){
            System.err.println("Error parsing book language info: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public BookInfo findBookInfoByProviderId(String providerId) {
        return bookInfoRepository.findByProviderId(providerId);
    }

    /* user 개발 완료 이후
    // bookInfo 삭제
    @Transactional
    public void deleteBookInfo(Long bookInfoId) {
    }

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
