package com.mmc.bookduck.domain.user.entity;

public enum UserRelationshipStatus {
    NONE,               // 관계 없음
    PENDING,           // 요청 중
    ACCEPT,            // 수락하기
    FRIEND,            // 친구
    SELF              // 나
}
