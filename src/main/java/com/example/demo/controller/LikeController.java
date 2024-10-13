package com.example.demo.controller;

import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.service.ILikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.constants.Constants.STATUS_201;

@Tag(name = "Like API", description = "API related to liking diaries")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class LikeController {

    private final ILikeService iLikeService;
    @Operation(summary = "Like a diary", description = "This API allows a user to like a specific diary.")
    @PostMapping("/users/{userId}/diaries/{diaryId}")
    public ResponseEntity<ResponseDto> plusLike(@PathVariable Long userId,
                                                @PathVariable Long diaryId){
        iLikeService.plusLikeToDiary(userId, diaryId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(STATUS_201, "like created success."));
    }
}
