package com.mmc.bookduck.domain.book.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record CustomBookUpdateDto (String title, String author, Long pageCount, String publisher, MultipartFile coverImage){

}
