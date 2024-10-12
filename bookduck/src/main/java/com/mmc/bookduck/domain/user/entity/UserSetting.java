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
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long userSettingId;

    @ColumnDefault("true")
    private boolean isPushAlarmEnabled;

    @ColumnDefault("true")
    private boolean isFriendRequestEnabled;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RecordFont recordFont;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", unique = true, updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public UserSetting(User user) {
        this.user = user;
        this.isPushAlarmEnabled = true;
        this.isFriendRequestEnabled = true;
        this.recordFont = RecordFont.NANUMGOTHIC;
    }

    public void updateIsPushAlarmEnabled(boolean isPushAlarmEnabled) {
        this.isPushAlarmEnabled = isPushAlarmEnabled;
    }
    public void updateIsFriendRequestEnabled(boolean isFriendRequestEnabled) {
        this.isFriendRequestEnabled = isFriendRequestEnabled;
    }
    public void updateRecordFont(RecordFont recordFont) {
        this.recordFont = recordFont;
    }
}
