package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank
    @Size(min = 2, max = 200)
    private String name;

    private String description;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal price;

    @Min(0)
    private Integer stockQuantity;

    @NotNull
    private Long categoryId;
}
