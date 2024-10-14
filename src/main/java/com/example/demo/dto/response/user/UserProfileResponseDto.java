package com.example.demo.dto.response.user;

import com.example.demo.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    private Long id;
    private String name;
    private Boolean premiumBadge;
    private Image profileImage;
}
