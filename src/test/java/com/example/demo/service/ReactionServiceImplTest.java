//package com.example.demo.service;
//
//import com.example.demo.dto.request.diary.DiaryRequestDto;
//import com.example.demo.dto.request.user.SignUpRequestDto;
//import com.example.demo.entity.Users;
//import com.example.demo.entity.enums.DiaryStatus;
//import com.example.demo.repository.ReactionRepository;
//import com.example.demo.repository.UserRepository;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class ReactionServiceImplTest {
//
//    @Autowired
//    IReactionService iReactionService;
//
//    @Autowired
//    IDiaryService iDiaryService;
//
//    @Autowired
//    ReactionRepository reactionRepository;
//
//    static long userId1;
//    static long userId2;
//
//    @BeforeAll
//    static void before(@Autowired IUserService userService) {
//        userId1 = userService.signUpAccount(new SignUpRequestDto("abc", "1234", "Tom"));
//        userId2 = userService.signUpAccount(new SignUpRequestDto("xyz", "0123", "Peter"));
//    }
//
//    @Test
//    void toggleDiaryLike() {
//        DiaryRequestDto dto = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
//        List<MultipartFile> images = new ArrayList<>();
//
//        long diaryId = iDiaryService.createDiary(userId1, dto, images);
//
//        Assertions.assertEquals(0, reactionRepository.count());
//        iReactionService.toggleDiaryLike(userId1, diaryId);
//        Assertions.assertEquals(1, reactionRepository.count());
//        iReactionService.toggleDiaryLike(userId1, diaryId);
//        Assertions.assertEquals(0, reactionRepository.count());
//
//    }
//
//    @AfterAll
//    static void after(@Autowired UserRepository userRepository) {
//        userRepository.deleteById(userId1);
//        userRepository.deleteById(userId2);
//    }
//}