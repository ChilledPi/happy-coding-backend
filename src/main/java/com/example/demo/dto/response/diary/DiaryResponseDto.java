package com.example.demo.dto.response.diary;

import com.example.demo.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;


@Getter
public class DiaryResponseDto {

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

    @Schema(description = "Number of likes on the diary", example = "100")
    private Integer likesCount;

    @Schema(description = "List of images associated with the diary")
    private List<ImageResponseDto> diaryImages;



    public DiaryResponseDto(Long diaryId, String name, String diaryTitle, Image profileImage, LocalDate date, Integer likesCount, List<Image> diaryImages) {
        this.diaryId = diaryId;
        this.name = name;
        this.diaryTitle = diaryTitle;
        this.profileImage = new ImageResponseDto(profileImage.getId(), profileImage.getUrl());
        this.date = date;
        this.likesCount = likesCount;
        this.diaryImages = diaryImages.stream().map(i -> new ImageResponseDto(i.getId(), i.getUrl())).toList();
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
