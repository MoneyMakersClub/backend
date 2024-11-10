package com.mmc.bookduck.domain.oneline.repository;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import java.util.Optional;

import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneLineRepository extends JpaRepository<OneLine, Long> {
    Optional<OneLine> findByUserBook(UserBook userBook);
    int countAllByUser(User user);
}
