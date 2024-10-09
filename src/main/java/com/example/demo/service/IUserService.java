package com.example.demo.service;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;

public interface IUserService {
    void signUpAccount(SignUpRequestDto signUpRequestDto);


    void signInAccount(LoginRequestDto loginRequestDto);

    UserProfileResponseDto getUserProfile(Long userId);


}
