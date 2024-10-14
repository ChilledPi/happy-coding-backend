package com.example.demo.dto.request.diary;

import com.example.demo.entity.enums.DiaryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class DiaryRequestDto {
    private double latitude;
    private double longitude;
    private String title;
    private String content;
    private LocalDate date;
    private DiaryStatus diaryStatus;
}
