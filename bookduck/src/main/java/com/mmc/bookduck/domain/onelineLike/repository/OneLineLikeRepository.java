package com.mmc.bookduck.domain.onelineLike.repository;

import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.onelineLike.entity.OneLineLike;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OneLineLikeRepository extends JpaRepository<OneLineLike, Long> {
    Optional<OneLineLike> findByOneLineAndUser(OneLine oneLine, User user); // OneLine에 User의 좋아요가 있는 지
}

