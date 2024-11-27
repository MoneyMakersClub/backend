package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Archive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByExcerpt_ExcerptId(Long excerptId);
    Optional<Archive> findByReview_ReviewId(Long reviewId);

    // 정확도순
    @Query(value = """
        SELECT * FROM (
            (SELECT 
                'EXCERPT' AS type,
                e.excerpt_id AS id,
                e.excerpt_content AS content,
                NULL AS title,
                e.visibility AS visibility,
                e.created_time AS created_time,
                b.title AS book_title,
                b.author AS book_author,
                CASE
                    WHEN e.excerpt_content = :keyword THEN 1
                    WHEN b.title = :keyword THEN 1
                    ELSE 0 
                END AS exact_match
             FROM excerpt e
             LEFT JOIN user_book ub ON e.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE e.user_id = :userId 
               AND (e.excerpt_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
            UNION ALL
            (SELECT 
                'REVIEW' AS type,
                r.review_id AS id,
                r.review_content AS content,
                r.review_title AS title,
                r.visibility AS visibility,
                r.created_time AS created_time,
                b.title AS book_title,
                b.author AS book_author,
                CASE
                    WHEN r.review_title = :keyword THEN 1
                    WHEN r.review_content = :keyword THEN 1
                    WHEN b.title = :keyword THEN 1
                    ELSE 0
                END AS exact_match
             FROM review r
             LEFT JOIN user_book ub ON r.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE r.user_id = :userId
               AND (r.review_title LIKE CONCAT('%', :keyword, '%') 
                    OR r.review_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
        ) AS union_results
        ORDER BY 
            exact_match DESC,
            created_time DESC
        """,
            countQuery = """
        SELECT COUNT(*) FROM (
            (SELECT e.excerpt_id 
             FROM excerpt e
             LEFT JOIN user_book ub ON e.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE e.user_id = :userId 
               AND (e.excerpt_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
            UNION ALL
            (SELECT r.review_id 
             FROM review r
             LEFT JOIN user_book ub ON r.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE r.user_id = :userId 
               AND (r.review_title LIKE CONCAT('%', :keyword, '%') 
                    OR r.review_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
        ) AS results
        """,
            nativeQuery = true)
    Page<Object[]> searchByAccuracy(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    // 최신순
    @Query(value = """
        SELECT * FROM (
            (SELECT 
                'EXCERPT' AS type,
                e.excerpt_id AS id,
                e.excerpt_content AS content,
                NULL AS title,
                e.visibility AS visibility,
                e.created_time AS created_time,
                b.title AS book_title,
                b.author AS book_author
             FROM excerpt e
             LEFT JOIN user_book ub ON e.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE e.user_id = :userId 
               AND (e.excerpt_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
            UNION ALL
            (SELECT 
                'REVIEW' AS type,
                r.review_id AS id,
                r.review_content AS content,
                r.review_title AS title,
                r.visibility AS visibility,
                r.created_time AS created_time,
                b.title AS book_title,
                b.author AS book_author
             FROM review r
             LEFT JOIN user_book ub ON r.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE r.user_id = :userId
               AND (r.review_title LIKE CONCAT('%', :keyword, '%') 
                    OR r.review_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
        ) AS union_results
        ORDER BY created_time DESC
        """,
            countQuery = """
        SELECT COUNT(*) FROM (
            (SELECT e.excerpt_id 
             FROM excerpt e
             LEFT JOIN user_book ub ON e.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE e.user_id = :userId 
               AND (e.excerpt_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
            UNION ALL
            (SELECT r.review_id 
             FROM review r
             LEFT JOIN user_book ub ON r.user_book_id = ub.user_book_id
             LEFT JOIN book_info b ON ub.book_info_id = b.book_info_id
             WHERE r.user_id = :userId 
               AND (r.review_title LIKE CONCAT('%', :keyword, '%') 
                    OR r.review_content LIKE CONCAT('%', :keyword, '%') 
                    OR b.title LIKE CONCAT('%', :keyword, '%')))
        ) AS results
        """,
            nativeQuery = true)
    Page<Object[]> searchByLatest(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
}
