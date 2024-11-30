package com.example.demo.dto.response.diary;

import com.example.demo.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MappingDiaryDetailsResponseDto {

    @Schema(description = "ID of the diary", example = "1")
    private Long diaryId;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Schema(description = "Title of the diary", example = "My Travel to Paris")
    private String diaryTitle;

    @Schema(description = "List of images associated with the profile")
    private ImageResponseDto profileImage;

    @Schema(description = "Date of the diary entry", example = "2023-10-12")
    private LocalDate date;

    @Schema(description = "Latitude of the diary entry", example = "37.7749")
    private double latitude;

    @Schema(description = "Longitude of the diary entry", example = "-122.4194")
    private double longitude;


    public MappingDiaryDetailsResponseDto(Long diaryId, String name, String diaryTitle, Image profileImage, LocalDate date, double latitude, double longitude ) {
        this.diaryId = diaryId;
        this.name = name;
        this.diaryTitle = diaryTitle;
        this.profileImage = new ImageResponseDto(profileImage.getId(), profileImage.getUrl());
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Getter
    @AllArgsConstructor
    private static class ImageResponseDto {

        @Schema(description = "ID of the image", example = "1")
        private Long imageId;

        @Schema(description = "URL of the image", example = "https://example.com/images/1")
        private String url;
    }
}
