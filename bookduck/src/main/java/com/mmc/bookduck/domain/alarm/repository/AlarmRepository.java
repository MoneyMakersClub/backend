package com.mmc.bookduck.domain.alarm.repository;

import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Slice<Alarm> findByReceiverOrderByCreatedTimeDesc(User user, Pageable pageable);
    Boolean existsByReceiverAndIsReadFalse(User user);
    void deleteAllBySender(User user);
    void deleteAllByReceiver(User user);
}