package com.mmc.bookduck.domain.user.entity;

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
    private Long xCoordinate;

    @NotNull
    private Long yCoordinate;

    @NotNull
    private Long width;

    @NotNull
    private Long height;

    private String title;

    private String content;

    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_home_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserHome userHome;

    @Builder
    public HomeBlock(BlockType blockType, Long xCoordinate, Long yCoordinate, Long width, Long height,
                     String title, String content, String imgPath, UserHome userHome) {
        this.blockType = blockType;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.width = width;
        this.height = height;
        this.title = title;
        this.content = content;
        this.imgPath = imgPath;
        this.userHome = userHome;
    }
}
