package com.example.demo.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
public class LoginRequestDto {

    @Schema(description = "Username for the user", example = "john_doe")
    private String username;

    @Schema(description = "Password for the user", example = "P@ssw0rd")
    private String password;
}