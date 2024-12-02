package com.example.demo.service;

import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.diary.DiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.DiaryResponseDto;
import com.example.demo.dto.response.diary.MappingDiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.UserDiaryResponseDto;
import com.example.demo.entity.Diary;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.DiaryStatus;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class DiaryServiceImplTest {

    @Autowired
    IDiaryService diaryService;
    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    ImageRepository imageRepository;

    static long userId1;
    static long userId2;

    @BeforeAll
    static void beforeAll(@Autowired IUserService userService) {
        userId1 = userService.signUpAccount(new SignUpRequestDto("abc", "1234", "Tom"));
        userId2 = userService.signUpAccount(new SignUpRequestDto("xyz", "0123", "Peter"));
    }

    @Test
    void createDiary() throws IOException {
        DiaryRequestDto dto1 = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
        List<MultipartFile> images = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("images/Squidward.jpeg");
        images.add(new MockMultipartFile("name1", "Squidward.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource.getPath()))));

        long diaryId = diaryService.createDiary(userId1, dto1, images);
        Diary findDiary = diaryRepository.findById(diaryId).get();

        Assertions.assertEquals(findDiary.getContent(), dto1.getContent());
        Assertions.assertNotNull(findDiary.getImages().stream().filter(i -> i.getName().equals("Squidward.jpeg")).findAny().get());
    }

    @Test
    void getUserDiary() throws IOException {
        DiaryRequestDto rqDto1 = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto2 = new DiaryRequestDto(38, 128, "today", "나는 오늘 밥을 먹었다", LocalDate.now(), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto3 = new DiaryRequestDto(38, 128, "tomorrow", "나는 내일 밥을 먹겠다", LocalDate.now().plusDays(1), DiaryStatus.PUBLIC);
        List<MultipartFile> images1 = new ArrayList<>();
        List<MultipartFile> images2 = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("images/Squidward.jpeg");
        images2.add(new MockMultipartFile("name1", "Squidward.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource.getPath()))));

        long diaryId1 = diaryService.createDiary(userId1, rqDto1, images1);
        long diaryId2 = diaryService.createDiary(userId1, rqDto2, images1);
        long diaryId3 = diaryService.createDiary(userId2, rqDto3, images2);

        UserDiaryResponseDto rsDto1 = diaryService.getUserDiary(userId1, diaryId1);
        UserDiaryResponseDto rsDto2 = diaryService.getUserDiary(userId1, diaryId2);
        UserDiaryResponseDto rsDto3 = diaryService.getUserDiary(userId2, diaryId3);

        Assertions.assertEquals(rsDto1.getContent(), rqDto1.getContent());
        Assertions.assertEquals(rsDto2.getContent(), rqDto2.getContent());
        Assertions.assertEquals(rsDto3.getContent(), rqDto3.getContent());
        Assertions.assertEquals(0, rsDto1.getImages().size());
        Assertions.assertEquals(0, rsDto2.getImages().size());
        Assertions.assertEquals(1, rsDto3.getImages().size());
    }

    @Test
    void patchDiary() throws IOException {
        DiaryRequestDto rqDto1 = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto2 = new DiaryRequestDto(38, 128, "today", "나는 오늘 밥을 먹었다", LocalDate.now(), DiaryStatus.PRIVATE);
        List<MultipartFile> images = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource1 = classLoader.getResource("images/SpongeBob.jpeg");
        URL resource2 = classLoader.getResource("images/Patrick.jpeg");
        URL resource3 = classLoader.getResource("images/Squidward.jpeg");
        images.add(new MockMultipartFile("name1", "SpongeBob.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource1.getPath()))));
        images.add(new MockMultipartFile("name2", "Patrick.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource2.getPath()))));

        List<MultipartFile> addImages = new ArrayList<>();
        List<Long> removeImages = new ArrayList<>();

        addImages.add(new MockMultipartFile("name3", "Squidward.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource3.getPath()))));

        long diaryId = diaryService.createDiary(userId1, rqDto1, images);

        removeImages.add(imageRepository.findByName("Patrick.jpeg").get().getId());

        diaryService.patchDiary(userId1, diaryId, rqDto2, addImages, removeImages);

        UserDiaryResponseDto diary = diaryService.getUserDiary(userId1, diaryId);

        Assertions.assertEquals(diary.getTitle(), rqDto2.getTitle());
        Assertions.assertEquals(diary.getContent(), rqDto2.getContent());
        Assertions.assertEquals(diary.getDate(), rqDto2.getDate());
        Assertions.assertEquals(diary.getDiaryStatus(), rqDto2.getDiaryStatus());

        Assertions.assertEquals(2, diary.getImages().size());
        Assertions.assertTrue(imageRepository.findByName("SpongeBob.jpeg").isPresent());
        Assertions.assertTrue(imageRepository.findByName("Patrick.jpeg").isEmpty());
        Assertions.assertTrue(imageRepository.findByName("Squidward.jpeg").isPresent());
    }

    @Test
    void deleteDiary() throws IOException {
        DiaryRequestDto rqDto1 = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto2 = new DiaryRequestDto(38, 128, "today", "나는 오늘 밥을 먹었다", LocalDate.now(), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto3 = new DiaryRequestDto(38, 128, "tomorrow", "나는 내일 밥을 먹겠다", LocalDate.now().plusDays(1), DiaryStatus.PUBLIC);
        List<MultipartFile> images = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource1 = classLoader.getResource("images/SpongeBob.jpeg");
        URL resource2 = classLoader.getResource("images/Patrick.jpeg");
        images.add(new MockMultipartFile("name1", "SpongeBob.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource1.getPath()))));
        images.add(new MockMultipartFile("name2", "Patrick.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource2.getPath()))));

        long diaryId1 = diaryService.createDiary(userId1, rqDto1, images);
        long diaryId2 = diaryService.createDiary(userId1, rqDto2, images);
        long diaryId3 = diaryService.createDiary(userId2, rqDto3, images);

        Assertions.assertEquals(3, diaryRepository.count());

        Diary diary1 = diaryRepository.findById(diaryId1).get();
        Diary diary2 = diaryRepository.findById(diaryId2).get();
        Diary diary3 = diaryRepository.findById(diaryId3).get();

        diaryService.deleteDiary(userId1, diaryId1);
        diaryService.deleteDiary(userId1, diaryId2);

        Assertions.assertEquals(0, imageRepository.findByDiary(diary1).size());
        Assertions.assertEquals(0, imageRepository.findByDiary(diary2).size());
        Assertions.assertEquals(2, imageRepository.findByDiary(diary3).size());

        diaryService.deleteDiary(userId2, diaryId3);

        Assertions.assertEquals(0, diaryRepository.count());
    }

//    @Test
//    void getAllDiaries(@Autowired IFollowingService followingService) {
//        DiaryRequestDto rqDto1 = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
//        DiaryRequestDto rqDto2 = new DiaryRequestDto(38, 128, "today", "나는 오늘 밥을 먹었다", LocalDate.now(), DiaryStatus.PUBLIC);
//        DiaryRequestDto rqDto3 = new DiaryRequestDto(38, 128, "tomorrow", "나는 내일 밥을 먹었다", LocalDate.now().plusDays(1), DiaryStatus.PUBLIC);
//
//        List<MultipartFile> images1 = new ArrayList<>();
//        List<MultipartFile> images2 = new ArrayList<>();
//        List<MultipartFile> images3 = new ArrayList<>();
//
//        long diaryId1 = diaryService.createDiary(userId2, rqDto1, images1);
//        long diaryId2 = diaryService.createDiary(userId2, rqDto2, images2);
//        long diaryId3 = diaryService.createDiary(userId2, rqDto3, images3);
//
//        diaryService.patchDiary(userId2, diaryId1, new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PRIVATE), new ArrayList<>(), new ArrayList<>());
//        diaryService.patchDiary(userId2, diaryId2, new DiaryRequestDto(38, 128, "today", "나는 오늘 밥을 먹었다", LocalDate.now(), DiaryStatus.FOLLOWER), new ArrayList<>(), new ArrayList<>());
//
//        followingService.following(userId1, userId2);
//
//        Pageable pageable = PageRequest.of(0, 3);
//        Page<MappingDiaryDetailsResponseDto> allDiaries1 = diaryService.getAllDiaries(userId1, DiaryStatus.PUBLIC, pageable);
//        Page<MappingDiaryDetailsResponseDto> allDiaries2 = diaryService.getAllDiaries(userId1, DiaryStatus.FOLLOWER, pageable);
//        Page<MappingDiaryDetailsResponseDto> allDiaries3 = diaryService.getAllDiaries(userId1, DiaryStatus.PRIVATE, pageable);
//
//        Assertions.assertEquals(1, allDiaries1.stream().count());
//        Assertions.assertEquals(2, allDiaries2.stream().count());
//        Assertions.assertEquals(3, allDiaries3.stream().count());
//    }

    @Test
    void getAllPublicDiaries() throws IOException {
        DiaryRequestDto rqDto1 = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto2 = new DiaryRequestDto(38, 128, "today", "나는 오늘 밥을 먹었다", LocalDate.now(), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto3 = new DiaryRequestDto(38, 128, "tomorrow", "나는 내일 밥을 먹겠다", LocalDate.now().plusDays(1), DiaryStatus.PUBLIC);
        List<MultipartFile> images1 = new ArrayList<>();
        List<MultipartFile> images2 = new ArrayList<>();
        List<MultipartFile> images3 = new ArrayList<>();

        long diaryId1 = diaryService.createDiary(userId1, rqDto1, images1);
        long diaryId2 = diaryService.createDiary(userId1, rqDto2, images2);
        long diaryId3 = diaryService.createDiary(userId2, rqDto3, images3);

        diaryService.patchDiary(userId1, diaryId1, new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PRIVATE), images3, List.of());
        Pageable pageable = PageRequest.of(0, 10);
        Page<MappingDiaryDetailsResponseDto> allPublicDiaries0 = diaryService.getAllPublicDiaries(userId1, pageable);
        List<MappingDiaryDetailsResponseDto> content0 = allPublicDiaries0.getContent();
        MappingDiaryDetailsResponseDto diariesDto0 = content0.get(0);

        Page<MappingDiaryDetailsResponseDto> allPublicDiaries1 = diaryService.getAllPublicDiaries(userId2, pageable);
        List<MappingDiaryDetailsResponseDto> content1 = allPublicDiaries1.getContent();
        MappingDiaryDetailsResponseDto diariesDto1 = content1.get(0);


        Assertions.assertEquals(1, content0.size());
        Assertions.assertEquals(diariesDto0.getDiaryTitle(), rqDto3.getTitle());
        Assertions.assertEquals(1, content1.size());
        Assertions.assertEquals(diariesDto1.getDiaryTitle(), rqDto2.getTitle());
    }


    @Test
    void getPublicDiary() throws IOException {
        DiaryRequestDto rqDto1 = new DiaryRequestDto(38, 128, "yesterday", "나는 어제 밥을 먹었다", LocalDate.now().minusDays(1), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto2 = new DiaryRequestDto(38, 128, "today", "나는 오늘 밥을 먹었다", LocalDate.now(), DiaryStatus.PUBLIC);
        DiaryRequestDto rqDto3 = new DiaryRequestDto(38, 128, "tomorrow", "나는 내일 밥을 먹겠다", LocalDate.now().plusDays(1), DiaryStatus.PUBLIC);
        List<MultipartFile> images1 = new ArrayList<>();
        List<MultipartFile> images2 = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("images/Squidward.jpeg");
        images2.add(new MockMultipartFile("name1", "Squidward.jpeg", "image/jpeg", Files.readAllBytes(Path.of(resource.getPath()))));

        long diaryId1 = diaryService.createDiary(userId1, rqDto1, images1);
        long diaryId2 = diaryService.createDiary(userId1, rqDto2, images1);
        long diaryId3 = diaryService.createDiary(userId2, rqDto3, images2);

        DiaryDetailsResponseDto rsDto1 = diaryService.getPublicDiary(diaryId1);
        DiaryDetailsResponseDto rsDto2 = diaryService.getPublicDiary(diaryId2);
        DiaryDetailsResponseDto rsDto3 = diaryService.getPublicDiary(diaryId3);

        Assertions.assertEquals(rsDto1.getContent(), rqDto1.getContent());
        Assertions.assertEquals(rsDto2.getContent(), rqDto2.getContent());
        Assertions.assertEquals(rsDto3.getContent(), rqDto3.getContent());
        Assertions.assertEquals(0, rsDto1.getImages().size());
        Assertions.assertEquals(0, rsDto2.getImages().size());
        Assertions.assertEquals(1, rsDto3.getImages().size());
    }

    @AfterAll
    static void after(@Autowired UserRepository userRepository) {
        userRepository.deleteById(userId1);
        userRepository.deleteById(userId2);
    }
}