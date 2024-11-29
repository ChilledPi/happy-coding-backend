package com.example.demo.service;

import com.example.demo.dto.response.follow.FollowResponseDto;
import com.example.demo.entity.Following;
import com.example.demo.entity.Users;
import com.example.demo.repository.FollowingRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FollowingServiceImpl implements IFollowingService{

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;

    public FollowingServiceImpl(UserRepository userRepository, FollowingRepository followingRepository) {
        this.userRepository = userRepository;
        this.followingRepository = followingRepository;
    }

    @Override
    public void following(Long userId, Long followingUserId) {
        Users users = userRepository.findById(userId).get();
        Users followingUser = userRepository.findById(followingUserId).get();
        Following following = Following.createFollowing();
        users.addFollowing(following); //follower user
        followingUser.addFollower(following);
        followingRepository.save(following);
    }

    @Override
    public void unfollowing(Long userId, Long followingUserId) {
        Users users = userRepository.findById(userId).get();
        Users followingUsers = userRepository.findById(followingUserId).get();
        Following following = users.getFollows().stream().filter(f -> Objects.equals(followingUserId, f.getFollower().getId())).findAny().get();
        users.getFollows().remove(following);
        followingUsers.getFollowers().remove(following);
        followingRepository.delete(following);
    }

    @Override
    public List<FollowResponseDto> getAllFollowings(Long userId) {
        Users users = userRepository.findById(userId).get();
        return followingRepository.findByFollow(users).stream()
                .map(following -> new FollowResponseDto(following.getFollower().getId(), following.getFollower().getName(), following.getFollow().getProfileImage()))
                .toList();
    }
}
