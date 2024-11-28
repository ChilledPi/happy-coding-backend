package com.example.demo.service;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;
import com.example.demo.entity.Image;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.ImageType;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements IUserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void signUpAccount(SignUpRequestDto signUpRequestDto) {

    }

    @Override
    public void signInAccount(LoginRequestDto loginRequestDto) {

    }

    @Override
    public UserProfileResponseDto getUserProfile(Long userId) {
        Users users = userRepository.findById(userId).get();
        return new UserProfileResponseDto(users.getId(), users.getName(), false, users.getProfileImage(), users.getTotalLikesCount());
    }

    @Override
    public void uploadProfilePicture(Long userId, MultipartFile image) {
        Users users = userRepository.findById(userId).get();
        users.setImage(Image.createImage(image.getOriginalFilename(), image.getName(), image.getOriginalFilename(), image.getSize(),  ImageType.USER_PROFILE, users, null));
    }
}
