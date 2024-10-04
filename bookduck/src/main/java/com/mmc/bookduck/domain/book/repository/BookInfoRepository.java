package com.mmc.bookduck.domain.book.repository;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
    BookInfo findByProviderId(String providerId);
}
