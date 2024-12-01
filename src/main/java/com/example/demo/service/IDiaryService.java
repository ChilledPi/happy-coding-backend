package com.example.demo.service;

import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.dto.response.diary.DiaryResponseDto;
import com.example.demo.dto.response.diary.DiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.MappingDiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.UserDiaryResponseDto;
import com.example.demo.entity.enums.DiaryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDiaryService {
    long createDiary(Long userId, DiaryRequestDto diaryRequestDto, List<MultipartFile> images);

    UserDiaryResponseDto getUserDiary(Long userId, Long diaryId);

    void patchDiary(Long userId, Long diaryId, DiaryRequestDto diaryRequestDto, List<MultipartFile> addImages, List<Long> removeImageIds);

    void deleteDiary(Long userId, Long diaryId);

    Page<MappingDiaryDetailsResponseDto> getAllDiaries(Long userId, DiaryStatus diaryStatus, Pageable pageable);

    Page<MappingDiaryDetailsResponseDto> getAllPublicDiaries(Pageable pageable);

    DiaryDetailsResponseDto getPublicDiary(Long diaryId);
}
