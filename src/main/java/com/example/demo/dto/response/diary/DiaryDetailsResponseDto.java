package com.example.demo.dto.response.diary;

import com.example.demo.entity.enums.DiaryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@AllArgsConstructor
public class DiaryDetailsResponseDto {

    @Schema(description = "Writer name", example = "John Doe")
    private String name;

    @Schema(description = "Latitude of the diary entry", example = "37.7749")
    private double latitude;

    @Schema(description = "Longitude of the diary entry", example = "-122.4194")
    private double longitude;

    @Schema(description = "Title of the diary entry", example = "My Trip to San Francisco")
    private String title;

    @Schema(description = "Content of the diary entry", example = "Visited famous landmarks in San Francisco.")
    private String content;

    @Schema(description = "Date of the diary entry", example = "2023-10-12")
    private LocalDate date;

    @Schema(description = "Number of likes on the diary", example = "100")
    private Integer likesCount;

    @Schema(description = "Status of the diary (PUBLIC/PRIVATE/FOLLOWER)", example = "PUBLIC")
    private DiaryStatus diaryStatus;

    @Schema(description = "List of images associated with the diary")
    private List<ImageResponseDto> images;

    @Getter
    @AllArgsConstructor
    private static class ImageResponseDto {

        @Schema(description = "ID of the image", example = "1")
        private Long imageId;

        @Schema(description = "URL of the image", example = "https://example.com/images/1")
        private String url;
    }
}
