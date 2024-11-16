package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExcerptRepository extends JpaRepository<Excerpt, Long> {

    long countByUser(User user);

    @Query("SELECT COUNT(e) FROM Excerpt e WHERE e.user = :user AND YEAR(e.createdTime) = :currentYear")
    long countByUserAndCreatedTimeThisYear(@Param("user") User user, @Param("currentYear") int currentYear);

    List<Excerpt> findExcerptByUserBookOrderByCreatedTimeDesc(UserBook userBook);

    @Query("SELECT e FROM Excerpt e WHERE e.userBook = :userBook AND (e.visibility = 'PUBLIC') ORDER BY e.createdTime DESC")
    List<Excerpt> findExcerptsByUserBookWithPublic(@Param("userBook") UserBook userBook);

    List<Excerpt> findAllByUserAndCreatedTimeAfter(User user, LocalDateTime createdTime);

    @Query("SELECT e FROM Excerpt e " +
            "JOIN e.userBook ub " +
            "WHERE (e.excerptContent LIKE %:keyword% " +
            "OR ub.bookInfo.title LIKE %:keyword% " +
            "OR ub.bookInfo.author LIKE %:keyword%) " +
            "AND e.user = :user " +
            "ORDER BY e.createdTime DESC")
    Page<Excerpt> searchAllByExcerptContentOrBookInfoTitleOrAuthorByUserAndCreatedTimeDesc(
            @Param("keyword") String keyword,
            @Param("user") User user,
            Pageable pageable);
}