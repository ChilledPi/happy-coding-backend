package com.example.demo.dto.response.follow;

import com.example.demo.dto.response.diary.DiaryResponseDto;
import com.example.demo.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


@Getter
public class FollowResponseDto {

    @Schema(description = "ID of the user being followed", example = "1")
    private Long userIds;

    @Schema(description = "Name of the user being followed", example = "John Doe")
    private String followNames;

    @Schema(description = "List of images associated with the profile")
    private List<ImageResponseDto> profileImages;

    public FollowResponseDto(Long userIds, String followNames, List<Image> profileImages) {
        this.userIds = userIds;
        this.followNames = followNames;
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
