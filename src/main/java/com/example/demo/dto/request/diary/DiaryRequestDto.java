package com.example.demo.dto.request.diary;

import com.example.demo.entity.enums.DiaryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class DiaryRequestDto {

    @Schema(description = "Latitude of the diary entry", example = "37.7749")
    private double latitude;

    @Schema(description = "Longitude of the diary entry", example = "-122.4194")
    private double longitude;

    @Schema(description = "Title of the diary entry", example = "My Travel to San Francisco")
    private String title;

    @Schema(description = "Content of the diary entry", example = "I visited several places in SF, including the Golden Gate Bridge.")
    private String content;

    @Schema(description = "Date of the diary entry", example = "2023-10-12")
    private LocalDate date;

    @Schema(description = "Status of the diary entry (PUBLIC/PRIVATE/FOLLOWER)", example = "PUBLIC")
    private DiaryStatus diaryStatus;
}