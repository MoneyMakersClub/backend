package com.mmc.bookduck.domain.book.repository;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    Optional<UserBook> findUserBookByUserBookId(Long userBookId);

    List<UserBook> findByUserAndReadStatus(User user, ReadStatus readStatus);

    // userbook 테이블과 bookinfo 테이블 조인해서 userbook의 user에 해당하는 bookinfo 정보 검색
    @Query("SELECT ub FROM UserBook ub JOIN ub.bookInfo b WHERE ub.user = :userId AND (" +
            "b.title LIKE %:keyword% OR b.author LIKE %:keyword% OR b.description LIKE %:keyword%)")
    List<UserBook> searchByUserIdAndKeyword(@Param("userId") User user, @Param("keyword") String keyword);

    Optional<UserBook> findByUserAndBookInfo(User user, BookInfo bookInfo);

    List<UserBook> findByBookInfo(BookInfo bookInfo);

    //최신순
    List<UserBook> findAllByUserOrderByCreatedTimeDesc(User user);

    //제목순
    @Query("SELECT ub FROM UserBook ub JOIN ub.bookInfo b WHERE ub.user = :user ORDER BY b.title ASC")
    List<UserBook> findAllByUserOrderByTitle(User user);

    //별점순
    @Query("SELECT ub From UserBook ub LEFT JOIN OneLineRating r ON r.userBook = ub WHERE ub.user = :user ORDER BY r.rating DESC")
    List<UserBook> findByUserOrderByRating(User user);

    List<UserBook> findAllByBookInfo(BookInfo bookInfo);

    List<UserBook> findAllByUser(User user);
}
