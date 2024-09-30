package com.mmc.bookduck.domain.user.repository;

import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);

    // 유저 검색
    @Query("SELECT u FROM User u WHERE (u.nickname LIKE %:keyword% ESCAPE '\\') AND u.userStatus = 'ACTIVE'")
    Page<User> findAllByNicknameContaining(@Param("keyword") String keyword, Pageable pageable);
}
