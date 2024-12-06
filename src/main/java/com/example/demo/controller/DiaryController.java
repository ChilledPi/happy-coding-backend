package com.example.demo.controller;

import com.example.demo.constants.Constants;
import com.example.demo.dto.request.diary.DiaryRequestDto;
import com.example.demo.dto.response.diary.DiaryResponseDto;
import com.example.demo.dto.response.diary.DiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.MappingDiaryDetailsResponseDto;
import com.example.demo.dto.response.diary.UserDiaryResponseDto;
import com.example.demo.dto.response.shared.PaginatedResponseDto;
import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.entity.enums.DiaryStatus;
import com.example.demo.service.IDiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Diary API", description = "API related to diary operations")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class DiaryController {
    private final IDiaryService iDiaryService;

    @Operation(summary = "Write a new diary entry",
            description = "This API creates a new diary entry for a user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Diary created successfully",
                            content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    @PostMapping(path = "/users/{userId}/diaries", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> writeDiary(
            @Parameter(description = "User ID", example = "1") @PathVariable Long userId,

            @RequestPart("diary")
            @Parameter(description = "Diary data containing the following fields:\n" +
                    "- **Latitude** (`double`): Latitude of the diary entry, e.g., `37.7749`\n" +
                    "- **Longitude** (`double`): Longitude of the diary entry, e.g., `-122.4194`\n" +
                    "- **Title** (`string`): Title of the diary entry, e.g., `My Travel to San Francisco`\n" +
                    "- **Content** (`string`): Content of the diary entry, e.g., `I visited several places in SF, including the Golden Gate Bridge.`\n" +
                    "- **Date** (`date`): Date of the diary entry, e.g., `2023-10-12`\n" +
                    "- **Diary Status** (`string`): Status of the diary entry, options are `PUBLIC`, `PRIVATE`, or `FOLLOWER`",
                    content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = DiaryRequestDto.class)))
            DiaryRequestDto diaryRequestDto,

            @RequestPart("images")
            @Parameter(description = "List of images to upload (Multipart file input)",
                    content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE,
                            array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))))
            List<MultipartFile> images) {

        Long id = iDiaryService.createDiary(userId, diaryRequestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(Constants.STATUS_201, "Diary created success.", id));
    }

    @Operation(summary = "Get a specific diary entry", description = "Retrieve a specific diary entry by its ID.")
    @GetMapping("/users/{userId}/diaries/{diaryId}")
    public ResponseEntity<UserDiaryResponseDto> getDiary(@Parameter(description = "User ID") @PathVariable Long userId,
                                                         @Parameter(description = "Diary ID") @PathVariable Long diaryId) {
        UserDiaryResponseDto userDiaryResponseDto = iDiaryService.getUserDiary(userId, diaryId);
        return ResponseEntity.status(HttpStatus.OK).body(userDiaryResponseDto);
    }

    @Operation(summary = "Update a diary entry", description = "Update a specific diary entry by adding or removing images.")
    @PatchMapping(path = "/users/{userId}/diaries/{diaryId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> updateDiary(@Parameter(description = "User ID") @PathVariable Long userId,
                                                   @Parameter(description = "Diary ID") @PathVariable Long diaryId,
                                                   @RequestPart("diary") @Parameter(description = "Diary data containing the following fields:\n" +
                                                               "- **Latitude** (`double`): Latitude of the diary entry, e.g., `37.7749`\n" +
                                                               "- **Longitude** (`double`): Longitude of the diary entry, e.g., `-122.4194`\n" +
                                                               "- **Title** (`string`): Title of the diary entry, e.g., `My Travel to San Francisco`\n" +
                                                               "- **Content** (`string`): Content of the diary entry, e.g., `I visited several places in SF, including the Golden Gate Bridge.`\n" +
                                                               "- **Date** (`date`): Date of the diary entry, e.g., `2023-10-12`\n" +
                                                               "- **Diary Status** (`string`): Status of the diary entry, options are `PUBLIC`, `PRIVATE`, or `FOLLOWER`",
                                                               content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE,
                                                                       schema = @Schema(implementation = DiaryRequestDto.class)))
                                                       DiaryRequestDto diaryRequestDto,
                                                   @RequestPart(value = "addImages", required = false)
                                                   @Parameter(description = "Images to add",
                                                           content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE,
                                                                   array =  @ArraySchema(schema = @Schema(type = "string", format = "binary"))))
                                                   List<MultipartFile> addImages,
                                                   @RequestPart(value = "removeImages", required = false)
                                                   @Parameter(description = "Image IDs to remove") List<Long> removeImageIds) {
        iDiaryService.patchDiary(userId, diaryId, diaryRequestDto, addImages, removeImageIds);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, "Patch diary success."));
    }

    @Operation(summary = "Delete a diary entry", description = "Delete a specific diary entry.")
    @DeleteMapping("/users/{userId}/diaries/{diaryId}")
    public ResponseEntity<ResponseDto> deleteDiary(@Parameter(description = "User ID") @PathVariable Long userId,
                                                   @Parameter(description = "Diary ID") @PathVariable Long diaryId) {
        iDiaryService.deleteDiary(userId, diaryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, "Delete diary success."));
    }

    @Operation(summary = "Get all diaries for a user", description = "Retrieve all diary entries for a user with pagination.")
    @GetMapping("/users/{userId}/diaries")
    public ResponseEntity<PaginatedResponseDto<MappingDiaryDetailsResponseDto>> getDiaries(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MappingDiaryDetailsResponseDto> diaries = iDiaryService.getAllDiaries(userId, DiaryStatus.FOLLOWER, pageable);
        PaginatedResponseDto<MappingDiaryDetailsResponseDto> response = PaginatedResponseDto.of(diaries);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get all my diaries", description = "Retrieve all my diary entries with pagination.")
    @GetMapping("/users/{userId}/my-diaries")
    public ResponseEntity<PaginatedResponseDto<MappingDiaryDetailsResponseDto>> getMyDiaries(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MappingDiaryDetailsResponseDto> diaries = iDiaryService.getMyDiaries(userId, pageable);
        PaginatedResponseDto<MappingDiaryDetailsResponseDto> response = PaginatedResponseDto.of(diaries);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get all public diaries", description = "Retrieve all public diary entries with pagination.")
    @GetMapping("/users/{userId}/public-diaries")
    public ResponseEntity<PaginatedResponseDto<MappingDiaryDetailsResponseDto>> getPublicDiaries(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MappingDiaryDetailsResponseDto> diaries = iDiaryService.getAllPublicDiaries(userId, pageable);
        PaginatedResponseDto<MappingDiaryDetailsResponseDto> response = PaginatedResponseDto.of(diaries);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get details of a public diary entry", description = "Retrieve the details of a specific public diary entry.")
    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryDetailsResponseDto> getPublicDiary(@Parameter(description = "Diary ID") @PathVariable Long diaryId) {
        DiaryDetailsResponseDto diaryDetailsResponseDto = iDiaryService.getPublicDiary(diaryId);
        return ResponseEntity.status(HttpStatus.OK).body(diaryDetailsResponseDto);
    }
}