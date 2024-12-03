package com.mmc.bookduck.domain.friend.service;

import com.mmc.bookduck.domain.alarm.service.AlarmByTypeService;
import com.mmc.bookduck.domain.alarm.service.AlarmService;
import com.mmc.bookduck.domain.friend.dto.common.FriendRequestUnitDto;
import com.mmc.bookduck.domain.friend.dto.request.FriendRequestDto;
import com.mmc.bookduck.domain.friend.dto.response.FriendRequestListResponseDto;
import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import com.mmc.bookduck.domain.friend.repository.FriendRepository;
import com.mmc.bookduck.domain.friend.repository.FriendRequestRepository;
import com.mmc.bookduck.domain.item.service.UserItemService;
import com.mmc.bookduck.domain.user.entity.UserSetting;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.user.service.UserSettingService;
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
    private final UserItemService userItemService;
    private final AlarmByTypeService alarmByTypeService;
    private final UserSettingService userSettingService;

    // 친구 요청 전송
    public void sendFriendRequest(FriendRequestDto requestDto) {
        User sender = userService.getCurrentUser();
        User receiver = userService.getActiveUserByUserId(requestDto.receiverId());
        // sender = receiver인 경우는 요청을 보낼 수 없음
        if (sender.getUserId().equals(receiver.getUserId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        // 이미 친구인지 확인
        if (friendRepository.findFriendBetweenUsers(sender.getUserId(), receiver.getUserId()).isPresent()) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_EXISTS);
        }
        // receiver의 친구 요청 설정이 false면 친구 요청 보낼 수 없음
        UserSetting receiverSetting = userSettingService.getUserSettingByUser(receiver);
        if (!receiverSetting.isFriendRequestEnabled()) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_DISABLED);
        }
        // 중복된 친구 요청 확인
        List<FriendRequest> existingRequests = friendRequestRepository.findAllFriendRequestsBetweenUsers(
                sender.getUserId(), receiver.getUserId(), FriendRequestStatus.PENDING);
        if (!existingRequests.isEmpty()) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }
        FriendRequest friendRequest = requestDto.toEntity(sender, receiver);
        friendRequestRepository.save(friendRequest);
        alarmByTypeService.createFriendRequestAlarm(sender, receiver);
    }

    // 친구 요청 취소
    public void cancelFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        // sender = currentUser 인지 확인
        User currentUser = userService.getCurrentUser();
        if (!request.getSender().getUserId().equals(currentUser.getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        // 친구 요청 삭제
        friendRequestRepository.delete(request);
        // 친구 요청 알림 삭제
        alarmByTypeService.deleteFriendRequestAlarm(request.getSender(), request.getReceiver());
    }

    // 받은 친구 요청 목록 조회
    @Transactional(readOnly = true)
    public FriendRequestListResponseDto getReceivedFriendRequests() {
        User currentUser = userService.getCurrentUser();
        List<FriendRequestUnitDto> receivedList = friendRequestRepository.findAllByReceiverUserIdAndFriendRequestStatus(currentUser.getUserId(), FriendRequestStatus.PENDING)
                .stream()
                .map(friendRequest -> FriendRequestUnitDto.from(
                        friendRequest,
                        friendRequest.getSender().getUserId(),
                        friendRequest.getSender().getNickname(),
                        friendRequest.getSender().isOfficial(),
                        userItemService.getUserItemEquippedListOfUser(friendRequest.getSender())
                ))
                .collect(Collectors.toList());
        return FriendRequestListResponseDto.from(receivedList);
    }

    // 보낸 친구 요청 목록 조회
    @Transactional(readOnly = true)
    public FriendRequestListResponseDto getSentFriendRequests() {
        User currentUser = userService.getCurrentUser();
        List<FriendRequestUnitDto> sentList = friendRequestRepository.findAllBySenderUserIdAndFriendRequestStatus(currentUser.getUserId(), FriendRequestStatus.PENDING)
                .stream()
                .map(friendRequest -> FriendRequestUnitDto.from(
                        friendRequest,
                        friendRequest.getReceiver().getUserId(),
                        friendRequest.getReceiver().getNickname(),
                        friendRequest.getReceiver().isOfficial(),
                        userItemService.getUserItemEquippedListOfUser(friendRequest.getReceiver())
                ))
                .collect(Collectors.toList());
        return FriendRequestListResponseDto.from(sentList);
    }

    // 친구 요청 거절
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        User currentUser = userService.getCurrentUser();

        // receiver = currentUser 인지 확인
        if (!request.getReceiver().getUserId().equals(currentUser.getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        request.setFriendRequestStatus(FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
        // 친구 요청 알림 삭제
        alarmByTypeService.deleteFriendRequestAlarm(request.getSender(), request.getReceiver());
    }

    @Transactional(readOnly = true)
    public FriendRequest getPendingFriendRequestBetweenUsers(User currentUser, User targetUser) {
        List<FriendRequest> existingRequests = friendRequestRepository.findAllFriendRequestsBetweenUsers(
                currentUser.getUserId(), targetUser.getUserId(), FriendRequestStatus.PENDING);
        if (existingRequests.isEmpty()) {
            return null;  // 요청이 없다면 null 반환
        }
        // 친구 요청 1개(1개여야만 함)를 반환
        return existingRequests.getFirst();
    }

    public void deleteFriendRequestsByUser(User user) {
        friendRequestRepository.deleteBySender(user);
        friendRequestRepository.deleteByReceiver(user);
    }

}
