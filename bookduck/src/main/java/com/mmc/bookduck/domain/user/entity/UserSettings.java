package com.mmc.bookduck.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long userSettingsId;

    @ColumnDefault("true")
    private boolean isPushAlarmEnabled;

    @ColumnDefault("true")
    private boolean isFriendRequestEnabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", unique = true, updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public UserSettings(User user) {
        this.user = user;
        this.isPushAlarmEnabled = true;
        this.isFriendRequestEnabled = true;
    }

    public void updateSettings(boolean isPushAlarmEnabled, boolean isFriendRequestEnabled) {
        this.isPushAlarmEnabled = isPushAlarmEnabled;
        this.isFriendRequestEnabled = isFriendRequestEnabled;
    }
}
