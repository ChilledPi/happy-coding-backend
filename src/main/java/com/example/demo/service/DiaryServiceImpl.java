package com.example.demo.service;

import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.dto.response.diary.DiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.MappingDiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.UserDiaryResponseDto;
import com.example.demo.entity.Diary;
import com.example.demo.entity.Image;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.DiaryStatus;
import com.example.demo.entity.enums.ImageType;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.ReactionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements IDiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final ImageRepository imageRepository;
    private final ReactionRepository reactionRepository;


    @Override
    @Transactional
    public long createDiary(Long userId, DiaryRequestDto diaryRequestDto, List<MultipartFile> images) {
        Users users = userRepository.findById(userId).get();
        Diary diary = Diary.createDiary(diaryRequestDto.getLatitude(), diaryRequestDto.getLongitude(), diaryRequestDto.getTitle(), diaryRequestDto.getContent(), diaryRequestDto.getDiaryStatus());
        users.addDiary(diary);
        diaryRepository.save(diary);
        images.stream().map(i -> Image.createImage(i.getOriginalFilename(), i.getName(), i.getContentType(), i.getSize(), ImageType.DIARY_IMAGE, users, diary))
                .forEach(i -> {
                    diary.addImage(i);
                    imageRepository.save(i);
                });
        return diary.getId();
    }

    @Override
    @Transactional
    public UserDiaryResponseDto getUserDiary(Long userId, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).get();
        return new UserDiaryResponseDto(diary.getUser().getName(), diary.getLatitude(), diary.getLongitude(), diary.getTitle(), diary.getContent(), diary.getDate(), diary.getLikesCount(), diary.getDiaryStatus(), diary.getImages());
    }

    @Override
    @Transactional
    public void patchDiary(Long userId, Long diaryId, DiaryRequestDto diaryRequestDto, List<MultipartFile> addImages, List<Long> removeImageIds) {
        Users users = userRepository.findById(userId).get();
        Diary diary = diaryRepository.findById(diaryId).get();
        diary.updateDiary(diaryRequestDto);
        //diaryRepository.save(diary);
        removeImageIds.forEach(i -> diary.getImages().removeIf(d -> Objects.equals(i, d.getId())));
        removeImageIds.forEach(imageRepository::deleteById);
        if (!addImages.isEmpty()) {
            addImages.stream().map(i -> Image.createImage(i.getOriginalFilename(), i.getName(), i.getContentType(), i.getSize(), ImageType.DIARY_IMAGE, users, diary))
                    .forEach(i -> {
                        diary.addImage(i);
                        imageRepository.save(i);
                    });
        }
    }

    @Override
    @Transactional
    public void deleteDiary(Long userId, Long diaryId) {
        Users users = userRepository.findById(userId).get();
        Diary diary = diaryRepository.findById(diaryId).get();
        List<Image> images = diary.getImages();
        if (images != null) {
            images.clear();
        }
        users.getDiaries().remove(diary);
        diaryRepository.delete(diary);
    }

    @Override
    @Transactional
    public Page<MappingDiaryDetailsResponseDto> getMyDiaries(Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId).get();
        return diaryRepository.findByUser(users, pageable)
                .map(diary -> new MappingDiaryDetailsResponseDto(diary.getUser().getId(), diary.getId(), diary.getUser().getName(), diary.getTitle(),
                        users.getProfileImage(), diary.getDate(), diary.getLatitude(), diary.getLongitude(),
                        diary.getReactions().stream().anyMatch(reaction -> Objects.equals(reaction.getUser().getId(), userId))));
    }

    @Override
    @Transactional
    public Page<MappingDiaryDetailsResponseDto> getAllDiaries(Long userId, DiaryStatus diaryStatus, Pageable pageable) {
        Users users = userRepository.findById(userId).get();
        return diaryRepository.findByFollowingUserAndDiaryStatusGreaterThanEqual(userId, diaryStatus, pageable)
                .map(diary -> new MappingDiaryDetailsResponseDto(diary.getUser().getId(), diary.getId(), diary.getUser().getName(), diary.getTitle(), diary.getUser().getProfileImage(), diary.getDate(), diary.getLatitude(), diary.getLongitude(),
                        diary.getReactions().stream().anyMatch(reaction -> Objects.equals(reaction.getUser().getId(), userId))));
    }

    @Override
    @Transactional
    public Page<MappingDiaryDetailsResponseDto> getAllPublicDiaries(Long userId, Pageable pageable) {
        Users users = userRepository.findById(userId).get();
        return diaryRepository.findByUserNotAndDiaryStatus(users, DiaryStatus.PUBLIC, pageable)
                .map(diary -> new MappingDiaryDetailsResponseDto(diary.getUser().getId(), diary.getId(), diary.getUser().getName(), diary.getTitle(), diary.getUser().getProfileImage(),  diary.getDate(), diary.getLatitude(), diary.getLongitude(),
                        diary.getReactions().stream().anyMatch(reaction -> Objects.equals(reaction.getUser().getId(), userId))));
    }

    @Override
    @Transactional
    public DiaryDetailsResponseDto getPublicDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).get();
        return new DiaryDetailsResponseDto(diary.getUser().getName(), diary.getLatitude(), diary.getLongitude(), diary.getTitle(), diary.getContent(), diary.getDate(), diary.getLikesCount(), diary.getDiaryStatus(), diary.getImages());
    }
}
