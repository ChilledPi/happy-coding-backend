package com.example.demo.service;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService{
    @Override
    public void signUpAccount(SignUpRequestDto signUpRequestDto) {

    }

    @Override
    public void signInAccount(LoginRequestDto loginRequestDto) {

    }

    @Override
    public UserProfileResponseDto getUserProfile(Long userId) {
        return null;
    }
}
