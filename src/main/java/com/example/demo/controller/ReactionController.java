package com.example.demo.controller;

import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.service.IReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.constants.Constants.STATUS_201;

@Tag(name = "Like API", description = "API related to liking diaries")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class ReactionController {

    private final IReactionService iReactionService;

    @Operation(summary = "Like a diary",
            description = "This API allows a user to like a specific diary.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Like created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
            })
    @PostMapping("/users/{userId}/diaries/{diaryId}")
    public ResponseEntity<ResponseDto> plusLike(
            @Parameter(description = "The ID of the user who likes the diary", example = "1") @PathVariable Long userId,
            @Parameter(description = "The ID of the diary being liked", example = "123") @PathVariable Long diaryId) {

        iReactionService.plusLikeToDiary(userId, diaryId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(STATUS_201, "Like created successfully."));
    }
}
