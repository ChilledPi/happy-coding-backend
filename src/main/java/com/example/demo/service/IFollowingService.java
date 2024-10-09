package com.example.demo.service;

import com.example.demo.dto.response.follow.FollowListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFollowingService {
    Page<FollowListResponseDto> getAllFollowings(Pageable pageable);

    void following(Long userId, Long followingUserId);

    void unfollowing(Long userId, Long followingUserId);
}
