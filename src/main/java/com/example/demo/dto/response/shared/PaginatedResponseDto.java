package com.example.demo.dto.response.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@AllArgsConstructor
public class PaginatedResponseDto<T> {

    @Schema(description = "Content of the current page")
    private List<T> content;

    @Schema(description = "Current page number", example = "1")
    private int currentPage;

    @Schema(description = "Total number of pages", example = "10")
    private int totalPages;

    @Schema(description = "Total number of items", example = "100")
    private long totalItems;

    public static <T> PaginatedResponseDto<T> of(Page<T> page) {
        return new PaginatedResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}