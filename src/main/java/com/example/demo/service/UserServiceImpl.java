package com.example.demo.service;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;
import com.example.demo.entity.Image;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.ImageType;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO 디폴트 프로필 이미지
    @Override
    @Transactional
    public long signUpAccount(SignUpRequestDto signUpRequestDto) {
        if (!userRepository.existsByName(signUpRequestDto.getName())) {
            Users user = Users.createUser(signUpRequestDto.getUsername(), signUpRequestDto.getPassword(), signUpRequestDto.getName());
            user.changeProfileImage(Image.createImage("default", "default", "default", 1L, ImageType.USER_PROFILE, null, null));
            return userRepository.save(user).getId();
        }
        return -1L;
    }

    @Override
    public void signInAccount(LoginRequestDto loginRequestDto) {

    }

    @Override
    @Transactional
    public UserProfileResponseDto getUserProfile(Long userId) {
        Users users = userRepository.findById(userId).get();
        return new UserProfileResponseDto(users.getId(), users.getName(), false, users.getProfileImage(), users.getTotalLikesCount(), users.getDiaries().size(), users.getFollows().size());
    }

    @Override
    @Transactional
    public void uploadProfilePicture(Long userId, MultipartFile image) {
        Users users = userRepository.findById(userId).get();
        users.changeProfileImage(Image.createImage(image.getOriginalFilename(), image.getName(), image.getOriginalFilename(), image.getSize(), ImageType.USER_PROFILE, users, null));
    }
}
