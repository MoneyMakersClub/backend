package com.mmc.bookduck.domain.alarm.repository;

import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Boolean existsByReceiverAndIsReadFalseAndAlarmType(User user, AlarmType badgeUnlocked);
    Boolean existsByReceiverAndIsReadFalseAndAlarmTypeNot(User receiver, AlarmType alarmType);

    @Query("SELECT a FROM Alarm a WHERE a.receiver = :user AND a.alarmType <> 'ANNOUNCEMENT' ORDER BY a.createdTime DESC")
    Page<Alarm> findByReceiverAndNotAnnouncementOrderByCreatedTimeDesc(@Param("user") User user, Pageable pageable);

    Page<Alarm> findByAlarmTypeOrderByCreatedTimeDesc(@NotNull AlarmType alarmType, Pageable pageable);

    void deleteAllBySender(User user);
    void deleteAllByReceiver(User user);

    void deleteByReceiverAndCreatedTimeBefore(User user, LocalDateTime threeMonthsAgo);
}