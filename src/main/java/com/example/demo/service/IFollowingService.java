package com.example.demo.service;

import com.example.demo.dto.response.follow.FollowResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFollowingService {

    void following(Long userId, Long followingUserId);

    void unfollowing(Long userId, Long followingUserId);

    List<FollowResponseDto> getAllFollowings(Long userId);
}
