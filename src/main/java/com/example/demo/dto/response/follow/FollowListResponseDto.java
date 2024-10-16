package com.example.demo.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@AllArgsConstructor
public class FollowListResponseDto {

    @Schema(description = "List of follow information")
    private List<FollowInfo> followInfos;

    @Getter
    @AllArgsConstructor
    public static class FollowInfo {

        @Schema(description = "ID of the user being followed", example = "1")
        private Long userIds;

        @Schema(description = "Name of the user being followed", example = "John Doe")
        private String followNames;
    }
}
