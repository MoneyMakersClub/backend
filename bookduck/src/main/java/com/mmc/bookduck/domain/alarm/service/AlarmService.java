package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.response.AlarmListResponseDto;
import com.mmc.bookduck.domain.alarm.dto.response.AlarmUnitDto;
import com.mmc.bookduck.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;
import com.mmc.bookduck.domain.alarm.repository.AlarmRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;
    private final UserService userService;

    // 최근 알림 목록 읽어오기
    public AlarmListResponseDto getAndReadRecentAlarms(){
        Pageable pageable = PageRequest.of(0, 30);
        List<AlarmUnitDto> alarmUnitDtos = getAndReadAlarms(pageable);
        return AlarmListResponseDto.fromAlarmUnitDto(alarmUnitDtos);
    }

    // 팔로우 알림 생성
    public void createFriendRequestAlarm(User sender, User receiver) {
        String message = MessageFormat.format(AlarmType.FRIEND_REQUEST.getMessagePattern(), sender.getNickname());
        Alarm alarm = Alarm.builder()
                .alarmType(AlarmType.FRIEND_REQUEST)
                .message(message)
                .sender(sender)
                .receiver(receiver)
                .build();
        alarmRepository.save(alarm);
        emitterService.notify(receiver.getUserId(), AlarmDefaultDataDto.from(true), "new alarm");
    }

    public void deleteAllAlarmsOfMember(User user){
        alarmRepository.deleteAllBySender(user);
        alarmRepository.deleteAllByReceiver(user);
    }

    private List<AlarmUnitDto> getAndReadAlarms(Pageable pageable) {
        List<AlarmUnitDto> alarmList = new ArrayList<>();
        User currentUser = userService.getCurrentUser();
        Slice<Alarm> alarmSlice = alarmRepository.findByReceiverOrderByCreatedTimeDesc(currentUser, pageable);
        if (alarmSlice != null && alarmSlice.hasContent()) {
            for (Alarm alarm : alarmSlice) {
                String message = switch (alarm.getAlarmType()){
                    case FRIEND_REQUEST -> MessageFormat.format(AlarmType.FRIEND_REQUEST.getMessagePattern(), alarm.getSender().getNickname(), alarm.getSender().getHandle());
                    case DEFAULT -> MessageFormat.format(AlarmType.DEFAULT.getMessagePattern(), alarm.getSender().getNickname(), alarm.getSender().getHandle());
                };
                alarmList.add(AlarmUnitDto.from(alarm, message));
                alarm.readAlarm();
            }
            alarmRepository.saveAll(alarmSlice);
        }
        return alarmList;
    }
}
