package com.mmc.bookduck.domain.friend.dto.response;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;

public record FriendRequestResponseDTO(
        Long requestId,
        Long senderId,
        Long receiverId,
        FriendRequestStatus friendRequestStatus
) {
    public static FriendRequestResponseDTO from(FriendRequest friendRequest) {
        return new FriendRequestResponseDTO(
                friendRequest.getRequestId(),
                friendRequest.getSender().getId(),
                friendRequest.getReceiver().getId(),
                friendRequest.getFriendRequestStatus()
        );
    }
}
