package com.example.demo.dto.response.diary;

import com.example.demo.entity.Image;
import com.example.demo.entity.enums.DiaryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserDiaryResponseDto {
    private double latitude;
    private double longitude;
    private String title;
    private String content;
    private LocalDate date;
    private Integer likesCount;
    private DiaryStatus diaryStatus;
    private List<Image> images;
}
