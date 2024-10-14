package com.example.demo.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FollowListResponseDto {
    private List<FollowInfo> followInfos;

    @Getter
    @AllArgsConstructor
    public static class FollowInfo {
        private Long userIds;
        private String followNames;
    }
}
