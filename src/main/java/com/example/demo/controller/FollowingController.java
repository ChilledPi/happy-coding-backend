package com.example.demo.controller;

import com.example.demo.constants.Constants;
import com.example.demo.dto.response.diary.DiariesResponseDto;
import com.example.demo.dto.response.follow.FollowListResponseDto;
import com.example.demo.dto.response.shared.PaginatedResponseDto;
import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.service.IFollowingService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Get a list of users being followed", description = "This API retrieves the list of users followed by a specific user.")
    @GetMapping("/users/{userId}/follows")
    public ResponseEntity<PaginatedResponseDto<FollowListResponseDto>> getFollowingList(@PathVariable Long userId,
                                                                                        @RequestParam(defaultValue = "0") int page,
                                                                                        @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FollowListResponseDto> followList  = iFollowingService.getAllFollowings(userId, pageable);
        PaginatedResponseDto<FollowListResponseDto> response = PaginatedResponseDto.of(followList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Follow a user", description = "This API allows a user to follow another user.")
    @PostMapping("/users/{userId}/follows/{followingUserId}")
    public ResponseEntity<ResponseDto> follow(@PathVariable Long userId,
                                              @PathVariable Long followingUserId){
        iFollowingService.following(userId, followingUserId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(Constants.STATUS_201, "Following created success."));

    }
    @Operation(summary = "Unfollow a user", description = "This API allows a user to unfollow another user.")
    @DeleteMapping("/users/{userId}/follows/{followingUserId}")
    public ResponseEntity<ResponseDto> unfollow(@PathVariable Long userId,
                                                @PathVariable Long followingUserId){
        iFollowingService.unfollowing(userId, followingUserId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, "Unfollow success."));

    }
}
