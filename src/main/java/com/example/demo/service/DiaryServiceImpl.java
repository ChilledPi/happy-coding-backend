package com.example.demo.service;

import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.dto.response.diary.DiaryResponseDto;
import com.example.demo.dto.response.diary.DiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.UserDiaryResponseDto;
import com.example.demo.entity.enums.DiaryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DiaryServiceImpl implements IDiaryService{
    @Override
    public void createDiary(Long userId, DiaryRequestDto diaryRequestDto, List<MultipartFile> images) {

    }

    @Override
    public UserDiaryResponseDto getUserDiary(Long userId, Long diaryId) {
        return null;
    }

    @Override
    public void patchDiary(Long userId, Long diaryId, DiaryRequestDto diaryRequestDto, List<MultipartFile> addImages, List<Long> removeImageIds) {

    }

    @Override
    public void deleteDiary(Long userId, Long diaryId) {

    }

    @Override
    public Page<DiaryResponseDto> getAllDiaries(Long userId, DiaryStatus diaryStatus, Pageable pageable) {
        return null;
    }

    @Override
    public Page<DiaryResponseDto> getAllPublicDiaries(Pageable pageable) {
        return null;
    }

    @Override
    public DiaryDetailsResponseDto getPublicDiary(Long diaryId) {
        return null;
    }
}
