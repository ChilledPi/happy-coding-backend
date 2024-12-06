package com.example.demo.dto.response.diary;

import com.example.demo.entity.Image;
import com.example.demo.util.ImageUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MappingDiaryDetailsResponseDto {

    private Long userId;

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

    private boolean isLiked;



    public MappingDiaryDetailsResponseDto(Long userId, Long diaryId, String name, String diaryTitle, Image profileImage, LocalDate date, double latitude, double longitude, boolean isLiked) {
        this.userId = userId;
        this.diaryId = diaryId;
        this.name = name;
        this.diaryTitle = diaryTitle;
        // 조건 추가: profileImage.getUrl()이 "default.jpg"가 아닌 경우에만 인코딩
        String encodedUrl = "default.jpg".equals(profileImage.getUrl())
                ? profileImage.getUrl()
                : ImageUtils.encodeImageToBase64(profileImage.getUrl());

        this.profileImage = new ImageResponseDto(
                profileImage.getId(),
                encodedUrl
        );
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isLiked = isLiked;
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
