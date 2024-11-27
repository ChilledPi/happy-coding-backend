package com.example.demo.service;

import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.dto.response.diary.DiaryResponseDto;
import com.example.demo.dto.response.diary.DiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.UserDiaryResponseDto;
import com.example.demo.entity.Diary;
import com.example.demo.entity.Image;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.DiaryStatus;
import com.example.demo.entity.enums.ImageType;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class DiaryServiceImpl implements IDiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final ImageRepository imageRepository;

    public DiaryServiceImpl(UserRepository userRepository, DiaryRepository diaryRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public long createDiary(Long userId, DiaryRequestDto diaryRequestDto, List<MultipartFile> images) {
        Users users = userRepository.findById(userId).get();
        Diary diary = Diary.createDiary(diaryRequestDto.getLatitude(), diaryRequestDto.getLongitude(), diaryRequestDto.getTitle(), diaryRequestDto.getContent());
        users.addDiary(diary);
        diaryRepository.save(diary);
        images.stream().map(i -> Image.createImage(i.getOriginalFilename(), "images/" + i.getOriginalFilename(), i.getContentType(), i.getSize(), ImageType.DIARY_IMAGE, users, diary))
                .forEach(i -> {
                    diary.addImage(i);
                    imageRepository.save(i);
                });
        return diary.getId();
    }

    @Override
    public UserDiaryResponseDto getUserDiary(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).get();
        return new UserDiaryResponseDto(diary.getUser().getName(), diary.getLatitude(), diary.getLongitude(), diary.getTitle(), diary.getContent(), diary.getDate(), diary.getLikesCount(), diary.getDiaryStatus(), diary.getImages());
    }

    @Override
    public void patchDiary(Long userId, Long diaryId, DiaryRequestDto diaryRequestDto, List<MultipartFile> addImages, List<Long> removeImageIds) {
        Users users = userRepository.findById(userId).get();
        Diary diary = diaryRepository.findById(diaryId).get();
        diary.updateDiary(diaryRequestDto);
        //diaryRepository.save(diary);
        removeImageIds.forEach(i -> diary.getImages().removeIf(d -> Objects.equals(i, d.getId())));
//        removeImageIds.forEach(imageRepository::deleteById);
        addImages.stream().map(i -> Image.createImage(i.getOriginalFilename(), "images/" + i.getOriginalFilename(), i.getContentType(), i.getSize(), ImageType.DIARY_IMAGE, users, diary))
                .forEach(i -> {
                    diary.addImage(i);
                    imageRepository.save(i);
                });
    }

    @Override
    public void deleteDiary(Long userId, Long diaryId) {
        Users users = userRepository.findById(userId).get();
        Diary diary = diaryRepository.findById(diaryId).get();
        users.getDiaries().remove(diary);
//        diaryRepository.delete(diary);
    }

    @Override
    public Page<DiaryResponseDto> getAllDiaries(Long userId, DiaryStatus diaryStatus, Pageable pageable) {
        Users users = userRepository.findById(userId).get();
        return diaryRepository.findByUserAndDiaryStatus(users, diaryStatus, pageable)
                .map(diary -> new DiaryResponseDto(diary.getId(), users.getName(), diary.getTitle(), users.getProfileImage(), diary.getDate(), diary.getLikesCount(), diary.getImages()));
    }

    @Override
    public Page<DiaryResponseDto> getAllPublicDiaries(Pageable pageable) {
        return diaryRepository.findByDiaryStatus(DiaryStatus.PUBLIC, pageable)
                .map(diary -> new DiaryResponseDto(diary.getId(), diary.getUser().getName(), diary.getTitle(), diary.getUser().getProfileImage(), diary.getDate(), diary.getLikesCount(), diary.getImages()));
    }

    @Override
    public DiaryDetailsResponseDto getPublicDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).get();
        return new DiaryDetailsResponseDto(diary.getUser().getName(), diary.getLatitude(), diary.getLongitude(), diary.getTitle(), diary.getContent(), diary.getDate(), diary.getLikesCount(), diary.getDiaryStatus(), diary.getImages());
    }
}
