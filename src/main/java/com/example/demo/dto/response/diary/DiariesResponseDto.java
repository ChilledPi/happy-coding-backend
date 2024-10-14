package com.example.demo.dto.response.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@AllArgsConstructor
public class DiariesResponseDto {

    @Schema(description = "List of diary information")
    private List<DiaryInfo> diaryInfos;

    @Getter
    @AllArgsConstructor
    public static class DiaryInfo {

        @Schema(description = "ID of the diary", example = "1")
        private Long diaryId;

        @Schema(description = "Title of the diary", example = "My Travel to Paris")
        private String diaryTitle;
    }
}
