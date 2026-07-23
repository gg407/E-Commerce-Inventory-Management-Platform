package com.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

public class CartDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddItemRequest {
        @NotNull
        private Long productId;

        @NotNull @Min(1)
        private Integer quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateItemRequest {
        @NotNull @Min(1)
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemDto {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartResponse {
        private Long id;
        private List<CartItemDto> items;
        private BigDecimal total;
    }
}
