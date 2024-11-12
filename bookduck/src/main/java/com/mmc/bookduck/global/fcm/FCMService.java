package com.mmc.bookduck.global.fcm;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FCMService {

    /**
     * 토큰 기반 전송 -> 특정 사용자의 디바이스에 개별적인 알림을 전송(특정 한명에게 전송)
     * @param fcmToken
     * @param scheduleNotificationDto
     */
    public void sendNotificationToToken(String fcmToken,
                                        ScheduleNotificationDto scheduleNotificationDto) {

        Message message = Message.builder()
                .setNotification(getnotification())
                .setToken(fcmToken)
                .putAllData(getData(scheduleNotificationDto))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("전송 성공: " + response);
        } catch (Exception e) {
            log.info("전송 실패: " + e);
        }
    }

    private Notification getnotification(){
        Notification notification = Notification.builder()
                .setTitle("새로운 알림")
                .setBody("알림이 생성되었습니다")
                .build();
        return notification;
    }

    private Map<String, String> getData(ScheduleNotificationDto scheduleNotificationDto) {
        Map<String, String> data = new HashMap<>();
        data.put("alarmId", scheduleNotificationDto.alarmId().toString());
        data.put("nickname", scheduleNotificationDto.nickname());
        data.put("alarmType", scheduleNotificationDto.alarmType().toString());
        data.put("message", scheduleNotificationDto.message());
        data.put("url", scheduleNotificationDto.url());
        data.put("createdTime", scheduleNotificationDto.createdTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return data;
    }
}
