package com.mmc.bookduck.global.fcm;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mmc.bookduck.domain.alarm.entity.PushAlarmFormat;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.repository.UserRepository;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.security.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final UserService userService;

    @Transactional
    public String getFcmToken(Long userId, String fcmToken) {
        // 해당 아이디 가진 유저가 존재하는지 검사
        User user = userService.getActiveUserByUserId(userId);
        user.setFcmToken(fcmToken);
        return "토큰이 성공적으로 저장되었습니다";
    }

    // 토큰 기반 전송
    public void sendPushMessage(String token, PushAlarmFormat pushAlarmFormat) {
        Message message = Message.builder().setNotification(Notification.builder()
                        .setTitle(pushAlarmFormat.getTitle())
                        .setBody(pushAlarmFormat.getBody())
                        .build())
                .setToken(token)  // 대상 디바이스의 등록 토큰
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("전송 성공: " + response);
        } catch (FirebaseMessagingException e) {
            log.info("전송 실패: " + e);
        }
    }
}
