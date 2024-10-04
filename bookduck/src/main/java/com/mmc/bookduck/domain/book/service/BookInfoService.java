package com.mmc.bookduck.domain.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.bookduck.domain.book.dto.common.BookInfoUnitDto;
import com.mmc.bookduck.domain.book.dto.response.BookListResponseDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookInfoService {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${google.books.api.key}")
    private String apiKey;


    public BookListResponseDto searchBookList(String keyword, Long page, Long size) {

        // 페이지 1부터 시작한다고 가정
        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/volumes")
                .queryParam("q", keyword)
                .queryParam("startIndex", (page-1))
                .queryParam("maxResults", size)
                .queryParam("key", apiKey)
                .toUriString();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // GET 요청
        ResponseEntity<String> apiResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String responseBody = apiResponse.getBody();
        return parseBookInfo(responseBody);
    }

    public BookListResponseDto parseBookInfo(String apiResult){

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(apiResult);
            JsonNode itemsNode = rootNode.path("items");

            List<BookInfoUnitDto> bookList = new ArrayList<>();

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

                bookList.add(new BookInfoUnitDto(title, authors, publisher, publishedYear, imgPath, providerId));
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
            // 네자리 연도로 추출
            return Long.parseLong(publishedDate.substring(0, 4));
        }
    }

    private String getTextNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            return node.get(fieldName).asText();
        }
        return null; // 필드가 없을 경우 null
    }
}
