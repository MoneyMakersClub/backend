package com.mmc.bookduck.domain.onelinerating.repository;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.onelinerating.entity.OneLineRating;
import java.util.Optional;

import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneLineRatingRepository extends JpaRepository<OneLineRating, Long> {
    Optional<OneLineRating> findByUserBook(UserBook userBook);
    int countAllByUser(User user);
}
