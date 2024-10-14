package com.example.demo.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@AllArgsConstructor
public class UserProfileResponseDto {

    @Schema(description = "ID of the user", example = "1")
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Schema(description = "Whether the user has a premium badge", example = "true")
    private Boolean premiumBadge;

    @Schema(description = "Profile image information")
    private ImageResponseDto profileImage;

    @Getter
    @AllArgsConstructor
    private static class ImageResponseDto {

        @Schema(description = "ID of the profile image", example = "1")
        private Long imageId;

        @Schema(description = "URL of the profile image", example = "https://example.com/profile/1")
        private String url;
    }
}