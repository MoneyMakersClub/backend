package com.mmc.bookduck.domain.user.entity;

import com.mmc.bookduck.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Builder
    public User(Long userId, String email, LoginType loginType, String nickname) {
        this.userId = userId;
        this.email = email;
        this.loginType = loginType;
        this.nickname = nickname;
        this.role = Role.ROLE_USER;
        this.userStatus = UserStatus.ACTIVE;
    }

    // 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // FCM 토큰 세팅
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
