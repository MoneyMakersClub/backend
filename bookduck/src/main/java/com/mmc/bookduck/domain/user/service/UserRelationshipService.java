package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import com.mmc.bookduck.domain.friend.service.FriendRequestService;
import com.mmc.bookduck.domain.friend.service.FriendService;
import com.mmc.bookduck.domain.user.dto.UserRelationshipStatusDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserRelationshipStatus;
import com.mmc.bookduck.domain.user.entity.UserSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRelationshipService {
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;
    private final UserSettingService userSettingService;

    @Transactional(readOnly = true)
    public UserRelationshipStatusDto getUserRelationshipStatus(User currentUser, User targetUser) {
        if (currentUser == null) {
            return new UserRelationshipStatusDto(UserRelationshipStatus.NONE, null, null);  // 비로그인 상태
        }
        if (currentUser.equals(targetUser)) {
            return new UserRelationshipStatusDto(UserRelationshipStatus.SELF, null, null);  // 본인
        }

        // 한 번의 쿼리로 친구 관계와 친구 요청 상태를 가져오기
        Optional<Friend> friend = friendService.getFriendBetweenUsers(currentUser, targetUser);
        if (friend.isPresent()) {
            return new UserRelationshipStatusDto(UserRelationshipStatus.FRIEND,
                    friend.get().getFriendId(),
                    null);
        }

        FriendRequest friendRequest = friendRequestService.getPendingFriendRequestBetweenUsers(currentUser, targetUser);
        if (friendRequest != null) {
            if (friendRequest.getSender().equals(currentUser)) {
                return new UserRelationshipStatusDto(UserRelationshipStatus.PENDING_REQUEST,
                        null,
                        friendRequest.getRequestId());
            } else {
                return new UserRelationshipStatusDto(UserRelationshipStatus.PENDING_ACCEPT,
                        null,
                        friendRequest.getRequestId());
            }
        }
        boolean isFriendRequestDisabled = !userSettingService.getUserSettingByUser(targetUser).isFriendRequestEnabled();
        if (isFriendRequestDisabled) {
            return new UserRelationshipStatusDto(UserRelationshipStatus.REQUEST_DISABLED, null, null);  // 관계 없음
        }
        return new UserRelationshipStatusDto(UserRelationshipStatus.NONE, null, null);  // 관계 없음
    }
}
