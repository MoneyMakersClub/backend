package com.mmc.bookduck.domain.book.repository;

import com.mmc.bookduck.domain.book.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
}
