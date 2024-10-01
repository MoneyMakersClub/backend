package com.mmc.bookduck.domain.alarm.repository;

import com.mmc.bookduck.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
