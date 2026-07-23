package com.ecommerce.config;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // already seeded
        }

        User admin = userRepository.save(User.builder()
                .email("admin@ecommerce.local")
                .passwordHash(passwordEncoder.encode("Admin@12345"))
                .role(Role.ADMIN)
                .build());
        cartRepository.save(Cart.builder().user(admin).build());

        User customer = userRepository.save(User.builder()
                .email("customer@ecommerce.local")
                .passwordHash(passwordEncoder.encode("Customer@12345"))
                .role(Role.CUSTOMER)
                .build());
        cartRepository.save(Cart.builder().user(customer).build());

        Category electronics = categoryRepository.save(Category.builder()
                .name("Electronics").slug("electronics").build());
        Category books = categoryRepository.save(Category.builder()
                .name("Books").slug("books").build());
        Category homeGoods = categoryRepository.save(Category.builder()
                .name("Home & Kitchen").slug("home-kitchen").build());

        productRepository.save(Product.builder()
                .name("Wireless Noise-Cancelling Headphones")
                .description("Over-ear Bluetooth headphones with active noise cancellation and 30-hour battery life.")
                .price(new BigDecimal("129.99"))
                .stockQuantity(50)
                .category(electronics)
                .build());

        productRepository.save(Product.builder()
                .name("Mechanical Keyboard")
                .description("Hot-swappable mechanical keyboard with RGB backlighting.")
                .price(new BigDecimal("89.50"))
                .stockQuantity(35)
                .category(electronics)
                .build());

        productRepository.save(Product.builder()
                .name("Clean Code")
                .description("A Handbook of Agile Software Craftsmanship by Robert C. Martin.")
                .price(new BigDecimal("34.99"))
                .stockQuantity(100)
                .category(books)
                .build());

        productRepository.save(Product.builder()
                .name("Stainless Steel Cookware Set")
                .description("10-piece cookware set, dishwasher safe, induction compatible.")
                .price(new BigDecimal("199.00"))
                .stockQuantity(20)
                .category(homeGoods)
                .build());

        productRepository.save(Product.builder()
                .name("Ceramic Non-Stick Pan")
                .description("12-inch ceramic-coated non-stick frying pan.")
                .price(new BigDecimal("42.75"))
                .stockQuantity(60)
                .category(homeGoods)
                .build());

        System.out.println("=================================================");
        System.out.println(" Demo data seeded successfully");
        System.out.println(" Admin login:    admin@ecommerce.local / Admin@12345");
        System.out.println(" Customer login: customer@ecommerce.local / Customer@12345");
        System.out.println("=================================================");
    }
}
