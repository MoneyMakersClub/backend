package com.mmc.bookduck.domain.friend.service;

import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import com.mmc.bookduck.domain.friend.repository.FriendRepository;
import com.mmc.bookduck.domain.friend.repository.FriendRequestRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserService userService;

    // 친구 요청 수락 (=친구 생성)
    public void acceptFriendRequest(Long requestId){
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        // currentUser = receiver여야만 수락 가능
        User currentUser = userService.getCurrentUser();
        if (!request.getReceiver().getUserId().equals(currentUser.getUserId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        User sender = request.getSender();
        User receiver = request.getReceiver();
        Friend friend = Friend.builder()
                        .user1(receiver)
                        .user2(sender)
                        .build();
        friendRepository.save(friend);

        request.setFriendRequestStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
    }
}
