package com.mmc.bookduck.domain.user.entity;

public enum UserRelationshipStatus {
    NONE,               // 관계 없음
    PENDING_REQUEST,           // 요청 중
    PENDING_ACCEPT,            // 수락하기
    FRIEND,            // 친구
    SELF              // 나
}
