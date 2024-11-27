package com.example.demo.controller;

import com.example.demo.constants.Constants;
import com.example.demo.dto.response.follow.FollowResponseDto;
import com.example.demo.dto.response.shared.PaginatedResponseDto;
import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.service.IFollowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Following API", description = "API related to following operations")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class FollowingController {
    private final IFollowingService iFollowingService;

    @Operation(summary = "Get a list of users being followed",
            description = "This API retrieves the list of users followed by a specific user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of users being followed retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PaginatedResponseDto.class)))
            })
    @GetMapping("/users/{userId}/follows")
    public ResponseEntity<PaginatedResponseDto<FollowResponseDto>> getFollowingList(
            @Parameter(description = "The ID of the user", example = "1") @PathVariable Long userId,
            @Parameter(description = "The page number for pagination", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size for pagination", example = "10") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<FollowResponseDto> followList = iFollowingService.getAllFollowings(userId, pageable);
        PaginatedResponseDto<FollowResponseDto> response = PaginatedResponseDto.of(followList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Follow a user",
            description = "This API allows a user to follow another user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully followed the user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class)))
            })
    @PostMapping("/users/{userId}/follows/{followingUserId}")
    public ResponseEntity<ResponseDto> follow(
            @Parameter(description = "The ID of the user performing the follow", example = "1") @PathVariable Long userId,
            @Parameter(description = "The ID of the user to be followed", example = "2") @PathVariable Long followingUserId) {

        iFollowingService.following(userId, followingUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(Constants.STATUS_201, "Following created success."));
    }

    @Operation(summary = "Unfollow a user",
            description = "This API allows a user to unfollow another user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully unfollowed the user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class)))
            })
    @DeleteMapping("/users/{userId}/follows/{followingUserId}")
    public ResponseEntity<ResponseDto> unfollow(
            @Parameter(description = "The ID of the user performing the unfollow", example = "1") @PathVariable Long userId,
            @Parameter(description = "The ID of the user to be unfollowed", example = "2") @PathVariable Long followingUserId) {

        iFollowingService.unfollowing(userId, followingUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, "Unfollow success."));
    }
}
