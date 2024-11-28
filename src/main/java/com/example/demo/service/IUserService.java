package com.example.demo.service;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    long signUpAccount(SignUpRequestDto signUpRequestDto);

    void signInAccount(LoginRequestDto loginRequestDto);

    UserProfileResponseDto getUserProfile(Long userId);


    void uploadProfilePicture(Long userId, MultipartFile image);
}
