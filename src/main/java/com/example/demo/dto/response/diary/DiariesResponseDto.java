package com.example.demo.dto.response.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DiariesResponseDto {
    private List<DiaryInfo> diaryInfos;

    @Getter
    @AllArgsConstructor
    public static class DiaryInfo {
        private Long diaryIds;
        private String diaryTitles;
    }
}
