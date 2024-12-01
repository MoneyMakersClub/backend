package com.mmc.bookduck.domain.user.entity;

import com.mmc.bookduck.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long userId;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private LoginType loginType;

    @Column(unique = true)
    @NotNull
    private String nickname;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus userStatus;

    private String fcmToken;

    @NotNull
    private Boolean isAnnouncementChecked;
    
    @ColumnDefault("false")
    private boolean isOfficial;

    @Builder
    public User(Long userId, String email, LoginType loginType, Role role, String nickname, boolean isOfficial) {
        this.userId = userId;
        this.email = email;
        this.loginType = loginType;
        this.nickname = nickname;
        this.role = role;
        this.userStatus = UserStatus.ACTIVE;
        this.isAnnouncementChecked = true;
        this.isOfficial = isOfficial;
    }

    // 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // FCM 토큰 세팅
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setIsAnnouncementChecked(boolean isAnnouncementChecked) {
        this.isAnnouncementChecked = isAnnouncementChecked;
    }

    public void updateStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void clearUserData() {
        this.email = "[deleted]";
        this.nickname = "알 수 없는 사용자";
        this.userStatus = UserStatus.DELETED;
        this.fcmToken = null;
        this.isOfficial = false;
        this.isAnnouncementChecked = true;
    }
}
