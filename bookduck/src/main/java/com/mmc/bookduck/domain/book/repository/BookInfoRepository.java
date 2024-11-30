package com.mmc.bookduck.domain.book.repository;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
    Optional<BookInfo> findByProviderId(String providerId);

    // providerId가 null이고, createdUserId와 제목 또는 저자가 검색어를 포함하는 책 검색 쿼리
    @Query("SELECT b FROM BookInfo b WHERE b.providerId IS NULL AND b.createdUserId = :createdUserId AND (b.title LIKE %:keyword% OR b.author LIKE %:keyword%)")
    List<BookInfo> searchByCreatedUserIdAndKeyword(@Param("createdUserId") Long createdUserId, @Param("keyword") String keyword);

    @Query("SELECT b " +
            "FROM BookInfo b " +
            "WHERE b.bookInfoId != :bookInfoId " +
            "   AND b.createdUserId IS NULL " +
            "   AND EXISTS (SELECT 1 FROM UserBook ub " +
            "       WHERE ub.bookInfo = b " +
            "           AND ub.user IN (SELECT u.user FROM UserBook u WHERE u.bookInfo.bookInfoId = :bookInfoId)) " +
            "GROUP BY b " +
            "ORDER BY COUNT(b) DESC")
    List<BookInfo> findRelatedBooksByBookInfoId(@Param("bookInfoId") Long bookInfoId);

    @Query("SELECT b FROM BookInfo b WHERE b.createdUserId = :userId")
    List<BookInfo> findCustomBookByCreatedUserId(@Param("userId") Long userId);
}
