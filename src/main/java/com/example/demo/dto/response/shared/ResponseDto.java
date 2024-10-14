package com.example.demo.dto.response.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
public class ResponseDto {

    @Schema(description = "Status code of the response", example = "200")
    private String statusCode;

    @Schema(description = "Message describing the status", example = "Success")
    private String statusMessage;
}