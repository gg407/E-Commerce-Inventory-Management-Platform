package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    private Long parentId;
}
