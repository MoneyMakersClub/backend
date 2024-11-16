package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExcerptRepository extends JpaRepository<Excerpt, Long> {

    long countByUser(User user);

    @Query("SELECT COUNT(e) FROM Excerpt e WHERE e.user = :user AND YEAR(e.createdTime) = :currentYear")
    long countByUserAndCreatedTimeThisYear(@Param("user") User user, @Param("currentYear") int currentYear);

    List<Excerpt> findExcerptByUserBookOrderByCreatedTimeDesc(UserBook userBook);

    @Query("SELECT e FROM Excerpt e WHERE e.userBook = :userBook AND (e.visibility = 'PUBLIC' OR e.visibility = 'FRIEND_ONLY') ORDER BY e.createdTime DESC")
    List<Excerpt> findExcerptsByUserBookWithPublicOrFriendOnly(@Param("userBook") UserBook userBook);

    List<Excerpt> findAllByUserAndCreatedTimeAfter(User user, LocalDateTime createdTime);
}