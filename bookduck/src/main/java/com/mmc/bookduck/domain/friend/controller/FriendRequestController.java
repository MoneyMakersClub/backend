package com.mmc.bookduck.domain.friend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Friend", description = "Friend 관련 API입니다.")
@RestController
@RequestMapping("/friendrequests")
@RequiredArgsConstructor
public class FriendRequestController {
}
