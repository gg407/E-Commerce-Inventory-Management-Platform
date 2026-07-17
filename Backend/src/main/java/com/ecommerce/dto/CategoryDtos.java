package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class CategoryDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryRequest {
        @NotBlank
        private String name;

        @NotBlank
        private String slug;

        private Long parentId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponse {
        private Long id;
        private String name;
        private String slug;
        private Long parentId;
    }
}
