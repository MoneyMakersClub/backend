package com.mmc.bookduck.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserHome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long userHomeId;

    @NotNull
    private String bio;

    @NotNull
    private String aiTags;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public UserHome(User user) {
        this.user = user;
        this.bio = "";
        this.aiTags = "";
    }

    // bio 변경
    public void updateBio(String bio) {
        this.bio = bio;
    }

    // aiTags 변경
    public void updateAiTags(String aiTags) {
        this.aiTags = aiTags;
    }
}
