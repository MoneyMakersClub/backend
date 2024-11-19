package com.mmc.bookduck.global.schedule;

import com.mmc.bookduck.domain.alarm.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AlarmCleanupTask {

    @Autowired
    private AlarmRepository alarmRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldAlarms() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        alarmRepository.deleteByCreatedTimeBefore(threeMonthsAgo);
    }
}