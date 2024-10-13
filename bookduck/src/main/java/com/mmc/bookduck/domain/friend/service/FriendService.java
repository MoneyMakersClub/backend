package com.mmc.bookduck.domain.friend.service;

import com.mmc.bookduck.domain.friend.dto.request.FriendRequestDTO;
import com.mmc.bookduck.domain.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;

    public void sendFriendRequest(FriendRequestDTO requestDto) {

    }
}
