package com.example.demo.service;

import com.example.demo.dto.response.follow.FollowResponseDto;
import com.example.demo.entity.Users;
import com.example.demo.repository.FollowingRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class FollowingServiceImplTest {

    @Autowired
    IFollowingService followingService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowingRepository followingRepository;

    static long userId1;
    static long userId2;
    static long userId3;

    @BeforeAll
    static void beforeAll(@Autowired UserRepository userRepository) {
        Users users1 = Users.createUser("abc", "1234", "Tom");
        Users users2 = Users.createUser("xyz", "0123", "Peter");
        Users users3 = Users.createUser("jkl", "0707", "Jack");
        userId1 = userRepository.save(users1).getId();
        userId2 = userRepository.save(users2).getId();
        userId3 = userRepository.save(users3).getId();
    }

    @Test
    void following() {
        followingService.following(userId1, userId2);
        followingService.following(userId1, userId3);
        followingService.following(userId2, userId1);

        Users user1 = userRepository.findById(userId1).get();
        Users user2 = userRepository.findById(userId2).get();
        Users user3 = userRepository.findById(userId3).get();

        Assertions.assertEquals(2, user1.getFollowers().size());
        Assertions.assertEquals(1, user2.getFollowers().size());
        Assertions.assertEquals(3, followingRepository.count());
    }

    @Test
    void unfollowing() {
        followingService.following(userId1, userId2);
        followingService.following(userId1, userId3);
        followingService.following(userId2, userId1);

        followingService.unfollowing(userId1, userId2);
        followingService.unfollowing(userId1, userId3);
        followingService.unfollowing(userId2, userId1);

        Users user1 = userRepository.findById(userId1).get();
        Users user2 = userRepository.findById(userId2).get();

        Assertions.assertEquals(followingRepository.count(), 0);
        Assertions.assertEquals(user1.getFollowers().size(), 0);
        Assertions.assertEquals(user2.getFollowers().size(), 0);
    }

    @Test
    void getAllFollowings() {
        followingService.following(userId1, userId2);
        followingService.following(userId1, userId3);

        Pageable pageable = PageRequest.of(0, 1);

        Page<FollowResponseDto> allFollowings0 = followingService.getAllFollowings(userId1, pageable);
        List<FollowResponseDto> content0 = allFollowings0.getContent();
        FollowResponseDto following0 = content0.get(0);

        Pageable nextPageable = pageable.next();
        Page<FollowResponseDto> allFollowings1 = followingService.getAllFollowings(userId1, nextPageable);
        List<FollowResponseDto> content1 = allFollowings1.getContent();
        FollowResponseDto following1 = content1.get(0);

        Assertions.assertEquals(userId2, following0.getUserIds());
        Assertions.assertEquals(userId3, following1.getUserIds());
    }
}