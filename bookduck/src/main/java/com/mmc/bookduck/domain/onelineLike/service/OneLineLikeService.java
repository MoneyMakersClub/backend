package com.mmc.bookduck.domain.onelineLike.service;

import com.mmc.bookduck.domain.alarm.service.AlarmByTypeService;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.oneline.service.OneLineService;
import com.mmc.bookduck.domain.onelineLike.entity.OneLineLike;
import com.mmc.bookduck.domain.onelineLike.repository.OneLineLikeRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OneLineLikeService {
    private final OneLineLikeRepository oneLineLikeRepository;
    private final OneLineService oneLineService;
    private final UserService userService;
    private final AlarmByTypeService alarmByTypeService;

    // 생성
    public void createOneLineLike(Long oneLineId) {
        OneLine oneLine = oneLineService.getOneLineById(oneLineId);
        User currentUser = userService.getCurrentUser();
        Optional<OneLineLike> existingLike = oneLineLikeRepository.findByOneLineAndUser(oneLine, currentUser);
        if (existingLike.isPresent()) {
            throw new CustomException(ErrorCode.ONELINELIKE_ALREADY_EXISTS);
        }
        OneLineLike oneLineLike = new OneLineLike(oneLine, currentUser);
        oneLine.addOneLineLike(oneLineLike);
        // 타 사용자일 떄만 알림 전송
        if (!currentUser.equals(oneLine.getUser()))
            alarmByTypeService.createOneLineLikeAlarm(currentUser, oneLine.getUser(), oneLine.getUserBook().getBookInfo());
        oneLineLikeRepository.save(oneLineLike);
    }

    // 삭제
    public void deleteOneLineLike(Long oneLineId) {
        OneLine oneLine = oneLineService.getOneLineById(oneLineId);
        User currentUser = userService.getCurrentUser();
        OneLineLike like = oneLineLikeRepository.findByOneLineAndUser(oneLine, currentUser)
                .orElseThrow(() -> new CustomException(ErrorCode.ONELINELIKE_NOT_FOUND));
        oneLine.removeOneLineLike(like);
        oneLineLikeRepository.delete(like);
    }
}
