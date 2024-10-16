package com.mmc.bookduck.domain.friend.service;

import com.mmc.bookduck.domain.friend.dto.common.FriendRequestUnitDto;
import com.mmc.bookduck.domain.friend.dto.request.FriendRequestDto;
import com.mmc.bookduck.domain.friend.dto.response.FriendRequestListResponseDto;
import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import com.mmc.bookduck.domain.friend.repository.FriendRepository;
import com.mmc.bookduck.domain.friend.repository.FriendRequestRepository;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserService userService;

    // 친구 요청 전송
    public void sendFriendRequest(FriendRequestDto requestDto) {
        User sender = userService.getActiveUserByUserId(requestDto.senderId());
        User receiver = userService.getActiveUserByUserId(requestDto.receiverId());
        // 이미 친구인지 확인
        if (friendRepository.findByUser1UserIdAndUser2UserId(sender.getUserId(), receiver.getUserId()).isPresent()) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_EXISTS);
        }
        // 중복된 친구 요청 확인
        if (friendRequestRepository.findBySenderUserIdAndReceiverUserIdAndFriendRequestStatus(sender.getUserId(), receiver.getUserId(), FriendRequestStatus.PENDING).isPresent() ||
                friendRequestRepository.findBySenderUserIdAndReceiverUserIdAndFriendRequestStatus(receiver.getUserId(), sender.getUserId(), FriendRequestStatus.PENDING).isPresent()) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }
        FriendRequest friendRequest = requestDto.toEntity(sender, receiver);
        friendRequestRepository.save(friendRequest);
    }

    // 친구 요청 취소
    public void cancelFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        friendRequestRepository.delete(request);
    }

    // 받은 친구 요청 목록 조회
    @Transactional(readOnly = true)
    public FriendRequestListResponseDto getReceivedFriendRequests() {
        User currentUser = userService.getCurrentUser();
        List<FriendRequestUnitDto> receivedList = friendRequestRepository.findByReceiverUserIdAndFriendRequestStatus(currentUser.getUserId(), FriendRequestStatus.PENDING)
                .stream()
                .map(FriendRequestUnitDto::from)
                .collect(Collectors.toList());
        return FriendRequestListResponseDto.from(receivedList);
    }

    // 보낸 친구 요청 목록 조회
    @Transactional(readOnly = true)
    public FriendRequestListResponseDto getSentFriendRequests() {
        User currentUser = userService.getCurrentUser();
        List<FriendRequestUnitDto> sentList = friendRequestRepository.findBySenderUserIdAndFriendRequestStatus(currentUser.getUserId(), FriendRequestStatus.PENDING)
                .stream()
                .map(FriendRequestUnitDto::from)
                .collect(Collectors.toList());
        return FriendRequestListResponseDto.from(sentList);
    }

    // 친구 요청 거절
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        request.setFriendRequestStatus(FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }
}
