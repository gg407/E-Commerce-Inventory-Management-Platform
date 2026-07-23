package com.ecommerce.service;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Electronics").slug("electronics").build();
        product = Product.builder()
                .id(1L)
                .name("Headphones")
                .description("Wireless")
                .price(new BigDecimal("89.99"))
                .stockQuantity(10)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getProductById_returnsMappedResponse_whenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Headphones");
        assertThat(response.getCategoryName()).isEqualTo("Electronics");
    }

    @Test
    void getProductById_throwsResourceNotFound_whenProductMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createProduct_savesAndReturnsMappedResponse() {
        ProductRequest request = ProductRequest.builder()
                .name("Keyboard")
                .description("Mechanical")
                .price(new BigDecimal("59.99"))
                .stockQuantity(5)
                .categoryId(1L)
                .build();

        Product saved = Product.builder()
                .id(2L)
                .name("Keyboard")
                .description("Mechanical")
                .price(new BigDecimal("59.99"))
                .stockQuantity(5)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.createProduct(request);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getName()).isEqualTo("Keyboard");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void reduceStock_throwsInsufficientStock_whenQuantityExceedsAvailable() {
        when(productRepository.findByIdWithLock(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.reduceStock(1L, 100))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    void reduceStock_decrementsStock_whenQuantityAvailable() {
        when(productRepository.findByIdWithLock(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.reduceStock(1L, 4);

        assertThat(product.getStockQuantity()).isEqualTo(6);
        verify(productRepository).save(product);
    }
}
