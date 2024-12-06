package com.example.demo.dto.response.user;

import com.example.demo.dto.response.diary.MappingDiaryDetailsResponseDto;
import com.example.demo.entity.Image;
import com.example.demo.util.ImageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;


@Getter
public class UserProfileResponseDto {

    @Schema(description = "ID of the user", example = "1")
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Schema(description = "Whether the user has a premium badge", example = "true")
    private Boolean premiumBadge;

    @Schema(description = "Profile image information")
    private ImageResponseDto profileImage;

    private int totalLikeCount;

    private int totalDiaryCount;

    private int totalFollowCount;

    public UserProfileResponseDto(Long id, String name, Boolean premiumBadge, Image profileImage, int totalLikeCount, int totalDiaryCount, int totalFollowCount) {
        this.id = id;
        this.name = name;
        this.premiumBadge = premiumBadge;
        // 조건 추가: profileImage.getUrl()이 "default.jpg"가 아닌 경우에만 인코딩
        String encodedUrl = "default.jpg".equals(profileImage.getUrl())
                ? profileImage.getUrl()
                : ImageUtils.encodeImageToBase64(profileImage.getUrl());

        this.profileImage = new ImageResponseDto(
                profileImage.getId(),
                encodedUrl
        );
        this.totalLikeCount = totalLikeCount;
        this.totalDiaryCount = totalDiaryCount;
        this.totalFollowCount = totalFollowCount;
    }

    @Getter
    @AllArgsConstructor
    private static class ImageResponseDto {

        @Schema(description = "ID of the profile image", example = "1")
        private Long imageId;

        @Schema(description = "URL of the profile image", example = "https://example.com/profile/1")
        private String url;
    }
}