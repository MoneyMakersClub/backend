package com.mmc.bookduck.domain.alarm.entity;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.common.CreatedTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Alarm extends CreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long alarmId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AlarmType alarmType;

    private String message;

    private String url;

    private String resourceName;

    private Long resourceId;

    private String resourceValue;

    @ColumnDefault("false")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    @Builder
    public Alarm(AlarmType alarmType, User sender, User receiver ,String message,
                 String url, String resourceName, Long resourceId, String resourceValue) {
        this.alarmType = alarmType;
        this.isRead = false;
        this.sender = sender; // null일 수 있음
        this.receiver = receiver;
        this.message = message;
        this.url = url;
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.resourceValue = resourceValue;
    }
    
    public void readAlarm() {
        this.isRead = true;
    }
}
