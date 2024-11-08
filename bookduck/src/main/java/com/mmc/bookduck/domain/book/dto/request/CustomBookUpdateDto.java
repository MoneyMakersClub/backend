package com.mmc.bookduck.domain.book.dto.request;


public record CustomBookUpdateDto (String title,
                                  String author,
                                  Long pageCount,
                                  String publisher){

}
