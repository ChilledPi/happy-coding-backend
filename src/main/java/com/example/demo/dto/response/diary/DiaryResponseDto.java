package com.example.demo.dto.response.diary;

import com.example.demo.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;


import io.swagger.v3.oas.annotations.media.Schema;

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
    private List<ImageResponseDto> profileImages;

    public DiaryResponseDto(Long diaryId, String name, String diaryTitle, List<Image> profileImages) {
        this.diaryId = diaryId;
        this.name = name;
        this.diaryTitle = diaryTitle;
        this.profileImages = profileImages.stream().map(i -> new ImageResponseDto(i.getId(), i.getUrl())).toList();
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
