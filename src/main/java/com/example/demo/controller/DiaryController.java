package com.example.demo.controller;

import com.example.demo.constants.Constants;
import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.dto.response.diary.DiariesResponseDto;
import com.example.demo.dto.response.diary.DiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.UserDiaryResponseDto;
import com.example.demo.dto.response.shared.PaginatedResponseDto;
import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.entity.enums.DiaryStatus;
import com.example.demo.service.IDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class DiaryController {
    private final IDiaryService iDiaryService;

    @PostMapping(path = "/users/{userId}/diaries", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> writeDiary(@PathVariable Long userId,
                                                  @RequestPart("diary") DiaryRequestDto diaryRequestDto,
                                                  @RequestPart("images") List<MultipartFile> images){
        iDiaryService.createDiary(userId, diaryRequestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(Constants.STATUS_201, "Diary created success."));
    }


    @GetMapping("/users/{userId}/diaries/{diaryId}")
    public ResponseEntity<UserDiaryResponseDto> getDiary(@PathVariable Long userId,
                                                         @PathVariable Long diaryId){
        UserDiaryResponseDto userDiaryResponseDto = iDiaryService.getUserDiary(userId, diaryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDiaryResponseDto);
    }

    @PatchMapping(path = "/users/{userId}/diaries/{diaryId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> updateDiary(@PathVariable Long userId,
                                                   @PathVariable Long diaryId,
                                                   @RequestPart(value = "addImages", required = false) List<MultipartFile> addImages,
                                                   @RequestPart(value = "removeImages", required = false) List<Long> removeImageIds){
        iDiaryService.patchDiary(userId, diaryId, addImages, removeImageIds);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, "patch diary success."));

    }

    @DeleteMapping("/users/{userId}/diaries/{diaryId}")
    public ResponseEntity<ResponseDto> deleteDiary(@PathVariable Long userId,
                                                   @PathVariable Long diaryId){
        iDiaryService.deleteDiary(userId, diaryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, "delete diary success."));
    }


    @GetMapping("/users/{userId}/diaries")
    public ResponseEntity<PaginatedResponseDto<DiariesResponseDto>> getDiaries(@PathVariable Long userId,
                                                                               @RequestParam DiaryStatus diaryStatus,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<DiariesResponseDto> diaries  = iDiaryService.getAllDiaries(userId, diaryStatus, pageable);
        PaginatedResponseDto<DiariesResponseDto> response = PaginatedResponseDto.of(diaries);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/diaries")
    public ResponseEntity<PaginatedResponseDto<DiariesResponseDto>> getPublicDiaries(@RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<DiariesResponseDto> diaries  = iDiaryService.getAllPublicDiaries(pageable);
        PaginatedResponseDto<DiariesResponseDto> response = PaginatedResponseDto.of(diaries);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryDetailsResponseDto> getPublicDiary(@PathVariable Long diaryId){
        DiaryDetailsResponseDto diaryDetailsResponseDto = iDiaryService.getPublicDiary(diaryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(diaryDetailsResponseDto);
    }

}
