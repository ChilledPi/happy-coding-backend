package com.example.demo.controller;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.shared.ResponseDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;
import com.example.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.constants.Constants.STATUS_200;
import static com.example.demo.constants.Constants.STATUS_201;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class UserController {

    private final IUserService iUserService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        iUserService.signUpAccount(signUpRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(STATUS_201, "sign-up success."));
    }


    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto> signup(@RequestBody LoginRequestDto loginRequestDto){
        iUserService.signInAccount(loginRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(STATUS_200, "login success."));
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long userId){
        UserProfileResponseDto userProfileResponseDto = iUserService.getUserProfile(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userProfileResponseDto);
    }




}
