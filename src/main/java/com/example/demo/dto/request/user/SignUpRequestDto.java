package com.example.demo.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {

    @Schema(description = "Username for the new user", example = "john_doe")
    private String username;

    @Schema(description = "Password for the new user", example = "P@ssw0rd")
    private String password;

    @Schema(description = "Full name of the new user", example = "John Doe")
    private String name;
}
