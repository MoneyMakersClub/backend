package com.mmc.bookduck.domain.userhome.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HomeBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long homeBlockId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BlockType blockType;

    @NotNull
    private Long blockIndex;

    private Long resourceId1;

    private Long resourceId2;

    private String text1;

    private String text2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_home_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserHome userHome;

    @Builder
    public HomeBlock(BlockType blockType, Long blockIndex, Long resourceId1, Long resourceId2,
                     String text1, String text2, UserHome userHome) {
        this.blockType = blockType;
        this.blockIndex = blockIndex;
        this.resourceId1 = resourceId1;
        this.resourceId2 = resourceId2;
        this.text1 = text1;
        this.text2 = text2;
        this.userHome = userHome;
    }
}
