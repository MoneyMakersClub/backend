package com.mmc.bookduck.domain.skin.entity;

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
public class UserSkin extends CreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long userSkinId;

    @ColumnDefault("false")
    private boolean isEquipped;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE) // 다대일 단방향이므로 설정
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE) // 다대일 단방향이므로 설정
    private Skin skin;

    @Builder
    public UserSkin(User user, Skin skin, boolean isEquipped) {
        this.user = user;
        this.skin = skin;
        this.isEquipped = isEquipped;
    }

    public void updateIsEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }
}