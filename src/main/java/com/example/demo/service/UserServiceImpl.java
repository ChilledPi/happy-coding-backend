package com.example.demo.service;

import com.example.demo.dto.request.user.LoginRequestDto;
import com.example.demo.dto.request.user.SignUpRequestDto;
import com.example.demo.dto.response.user.UserProfileResponseDto;
import com.example.demo.entity.Image;
import com.example.demo.entity.Users;
import com.example.demo.entity.enums.ImageType;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;



    //TODO 디폴트 프로필 이미지
    @Override
    @Transactional
    public long signUpAccount(SignUpRequestDto signUpRequestDto) {
        Optional<Users> byName = userRepository.findByName(signUpRequestDto.getName());
        if (byName.isEmpty()) {
            // 사용자 생성
            Users user = Users.createUser(signUpRequestDto.getUsername(), signUpRequestDto.getPassword(), signUpRequestDto.getName());

            // 기본 프로필 이미지 생성
            Image profileImage = Image.createImage(
                    "default.jpg",
                    "default.jpg",
                    "image/jpeg",
                    100L,
                    ImageType.USER_PROFILE,
                    user,
                    null);
            user.changeProfileImage(profileImage);

            // 사용자 저장 후 ID 반환
            return userRepository.save(user).getId();
        } else {
            return byName.get().getId();
        }
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
        String baseUploadDir = "/home/momakapa/happy-coding-backend/uploads";
        String uploadDir = baseUploadDir + "/user-images/" + users.getId();
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + uploadDir);
            }
        }
        try{
            String originalFilename = image.getOriginalFilename();
            String filePath = uploadDir + "/" + originalFilename;
            File file = new File(filePath);
            image.transferTo(file); // 로컬 디렉토리에 저장

            Image imageEntity = Image.createImage(
                    image.getOriginalFilename(),
                    filePath,
                    image.getOriginalFilename(),
                    image.getSize(),
                    ImageType.USER_PROFILE,
                    users,
                    null);
            imageRepository.save(imageEntity);
            users.changeProfileImage(imageEntity);
            userRepository.save(users);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
