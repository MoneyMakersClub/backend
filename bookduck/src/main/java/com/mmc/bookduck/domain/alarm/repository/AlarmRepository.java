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
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Boolean existsByReceiverAndIsReadFalse(User user);

    @Query("SELECT a FROM Alarm a WHERE a.receiver = :user ORDER BY a.createdTime DESC")
    Page<Alarm> findByReceiverOrderByCreatedTimeDesc(@Param("user") User user, Pageable pageable);

    void deleteBySender(User user);
    void deleteByReceiver(User user);
    void deleteByCreatedTimeBefore(LocalDateTime createdTime);

    List<Alarm> findAllByReceiverAndIsReadFalse(User user);

    void deleteByAlarmTypeAndSenderAndReceiver(AlarmType alarmType, User sender, User receiver);
}