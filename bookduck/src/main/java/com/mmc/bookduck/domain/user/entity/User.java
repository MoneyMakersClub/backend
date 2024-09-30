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

    @Column(unique = true)
    @NotNull
    private String nickname;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserStatus userStatus;

    @NotNull
    private LoginType loginType;

    private String birth;

    private String gender;

    private String country;

    @Builder
    public User(Long userId, String email, String nickname, Role role,
                LoginType loginType, String birth, String gender, String country) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.role = role != null ? role : Role.ROLE_USER;
        this.userStatus = UserStatus.ACTIVE;
        this.loginType = loginType;
        this.birth = birth;
        this.gender = gender;
        this.country = country;
    }

    // 닉네임 변경
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
