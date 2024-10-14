package com.example.demo.controller;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;
import com.example.demo.service.IUserService;
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
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.constants.Constants.STATUS_200;
import static com.example.demo.constants.Constants.STATUS_201;
@Tag(name = "User API", description = "API related to user operations")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class UserController {

    private final IUserService iUserService;

    @Operation(summary = "User sign-up",
            description = "This API allows a new user to sign-up.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
            })
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User sign-up details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignUpRequestDto.class)))
            @RequestBody SignUpRequestDto signUpRequestDto) {

        iUserService.signUpAccount(signUpRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(STATUS_201, "sign-up success."));
    }

    @Operation(summary = "User sign-in",
            description = "This API allows an existing user to log in.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
            })
    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto> signin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User login details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDto.class)))
            @RequestBody LoginRequestDto loginRequestDto) {

        iUserService.signInAccount(loginRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(STATUS_200, "login success."));
    }

    @Operation(summary = "Get user profile",
            description = "This API retrieves the profile of a specific user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserProfileResponseDto.class))),
            })
    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<UserProfileResponseDto> getProfile(
            @Parameter(description = "ID of the user to retrieve profile", example = "1")
            @PathVariable Long userId) {

        UserProfileResponseDto userProfileResponseDto = iUserService.getUserProfile(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userProfileResponseDto);
    }

    @Operation(summary = "Upload user profile picture",
            description = "This API allows a user to upload a profile picture.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseDto.class))),
            })
    @PostMapping(path = "/users/{userId}/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDto> uploadProfilePicture(
            @Parameter(description = "ID of the user uploading the profile picture", example = "1")
            @PathVariable Long userId,
            @RequestPart("image") @Parameter(description = "Profile picture file",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")))
            MultipartFile image) {

        iUserService.uploadProfilePicture(userId, image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(STATUS_200, "Profile picture uploaded successfully."));
    }
}
