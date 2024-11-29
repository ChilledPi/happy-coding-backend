package com.example.demo.service;

import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.follow.FollowResponseDto;
import com.example.demo.entity.Users;
import com.example.demo.repository.FollowingRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    static void beforeAll(@Autowired IUserService userService) {
        userId1 = userService.signUpAccount(new SignUpRequestDto("abc", "1234", "Tom"));
        userId2 = userService.signUpAccount(new SignUpRequestDto("xyz", "0123", "Peter"));
        userId3 = userService.signUpAccount(new SignUpRequestDto("jkl", "0707", "Jack"));
    }

    @Test
    void following() {
        followingService.following(userId1, userId2);
        followingService.following(userId1, userId3);
        followingService.following(userId2, userId1);

        Users user1 = userRepository.findById(userId1).get();
        Users user2 = userRepository.findById(userId2).get();
        Users user3 = userRepository.findById(userId3).get();

        System.out.println("user1.getFollows().size() = " + user1.getFollows().size());
        Assertions.assertEquals(2, user1.getFollows().size());
        Assertions.assertEquals(1, user2.getFollows().size());
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

        List<FollowResponseDto> allFollowings = followingService.getAllFollowings(userId1);
        FollowResponseDto following0 = allFollowings.get(0);
        FollowResponseDto following1 = allFollowings.get(1);

        Assertions.assertEquals(userId2, following0.getUserIds());
        Assertions.assertEquals(userId3, following1.getUserIds());
    }

    @AfterAll
    static void after(@Autowired UserRepository userRepository) {
        userRepository.deleteById(userId1);
        userRepository.deleteById(userId2);
        userRepository.deleteById(userId3);
    }
}