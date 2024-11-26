package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import com.mmc.bookduck.domain.friend.service.FriendRequestService;
import com.mmc.bookduck.domain.friend.service.FriendService;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserRelationshipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRelationshipService {
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;

    @Transactional(readOnly = true)
    public UserRelationshipStatus getUserRelationshipStatus(User currentUser, User targetUser) {
        if (currentUser == null) {
            return UserRelationshipStatus.NONE;  // 비로그인 상태
        }
        if (currentUser.equals(targetUser)) {
            return UserRelationshipStatus.SELF;  // 본인
        }
        if (friendService.isFriendWithCurrentUserOrNull(targetUser)) {
            return UserRelationshipStatus.FRIEND;  // 친구
        }

        // 친구 요청 상태 구별
        FriendRequest friendRequest = friendRequestService.getPendingFriendRequestBetweenUsers(currentUser, targetUser);
        if (friendRequest != null && friendRequest.getFriendRequestStatus() == FriendRequestStatus.PENDING) {
            if (friendRequest.getSender().equals(currentUser)) {
                return UserRelationshipStatus.PENDING;  // 요청 보낸 사용자 -> 요청 중
            } else {
                return UserRelationshipStatus.ACCEPT;  // 요청 받은 사용자 -> 수락하기
            }
        }
        return UserRelationshipStatus.NONE;  // 관계 없음
    }
}
