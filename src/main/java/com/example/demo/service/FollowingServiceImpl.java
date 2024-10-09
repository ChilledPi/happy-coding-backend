package com.example.demo.service;

import com.example.demo.dto.response.follow.FollowListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FollowingServiceImpl implements IFollowingService{
    @Override
    public Page<FollowListResponseDto> getAllFollowings(Pageable pageable) {
        return null;
    }

    @Override
    public void following(Long userId, Long followingUserId) {

    }

    @Override
    public void unfollowing(Long userId, Long followingUserId) {

    }
}
