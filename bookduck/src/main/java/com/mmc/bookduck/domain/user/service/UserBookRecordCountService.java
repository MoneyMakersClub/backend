package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserBookRecordCountService {
    private final ExcerptRepository excerptRepository;
    private final ReviewRepository reviewRepository;

    public long getUserBookRecordCount(User user) {
        long excerptCount = excerptRepository.countByUser(user);
        long reviewCount = reviewRepository.countByUser(user);
        return excerptCount + reviewCount;
    }
}
