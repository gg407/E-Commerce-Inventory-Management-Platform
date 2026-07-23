package com.ecommerce.service;

import com.ecommerce.dto.CartItemRequest;
import com.ecommerce.dto.CartResponse;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        Cart cart = getOrCreateCart();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(product.getId(), request.getQuantity());
        }

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst().orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(item);
        }
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart() {
        return mapToResponse(getOrCreateCart());
    }

    @Transactional
    public CartResponse updateItemQuantity(Long itemId, int quantity) {
        Cart cart = getOrCreateCart();
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        if (item.getProduct().getStockQuantity() < quantity) {
            throw new InsufficientStockException(item.getProduct().getId(), quantity);
        }
        item.setQuantity(quantity);
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(Long itemId) {
        Cart cart = getOrCreateCart();
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Transactional
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart() {
        User user = currentUser();
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartResponse.ItemDto> items = cart.getItems().stream()
                .map(i -> CartResponse.ItemDto.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .unitPrice(i.getProduct().getPrice())
                        .quantity(i.getQuantity())
                        .build())
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .items(items)
                .totalAmount(total)
                .build();
    }
}
