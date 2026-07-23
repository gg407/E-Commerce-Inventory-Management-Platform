package com.ecommerce.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long productId, int requestedQuantity) {
        super(String.format("Insufficient stock for product id %d. Requested: %d", productId, requestedQuantity));
    }
}
