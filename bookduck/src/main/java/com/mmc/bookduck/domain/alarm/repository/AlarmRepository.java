package com.mmc.bookduck.domain.alarm.repository;

import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Boolean existsByReceiverAndIsReadFalse(User user);
    Boolean existsByReceiverAndIsReadFalseAndAlarmType(User user, AlarmType badgeUnlocked);

    @Query("SELECT a FROM Alarm a WHERE a.receiver = :user ORDER BY a.createdTime DESC")
    Page<Alarm> findByReceiverOrderByCreatedTimeDesc(@Param("user") User user, Pageable pageable);

    void deleteAllBySender(User user);
    void deleteAllByReceiver(User user);
    void deleteByCreatedTimeBefore(LocalDateTime createdTime);
}