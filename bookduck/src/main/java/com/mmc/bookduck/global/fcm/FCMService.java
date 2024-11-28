package com.mmc.bookduck.global.fcm;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final UserService userService;

    @Transactional
    public void setFcmToken(Long userId, String fcmToken) {
        // 해당 아이디 가진 유저가 존재하는지 검사
        User user = userService.getActiveUserByUserId(userId);
        user.setFcmToken(fcmToken);
        log.info("User " + userId + "의 FCM 토큰이 성공적으로 저장되었습니다");
    }

    // 토큰 기반 전송
    public void sendPushMessage(String token, String alarmMessage) {
        // Data 페이로드 구성
        Map<String, String> data = new HashMap<>();
        data.put("title", alarmMessage);
        data.put("body", "북덕에서 확인하세요."); // 원하는 추가 데이터 포함
        // Message 생성
        Message message = Message.builder()
                .putAllData(data)  // Data 필드에 데이터를 추가
                .setToken(token)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("전송 성공: " + response);
        } catch (FirebaseMessagingException e) {
            log.info("전송 실패: " + e);
        }
    }
}
