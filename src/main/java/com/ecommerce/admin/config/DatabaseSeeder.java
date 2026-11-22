package com.ecommerce.admin.config;

import com.ecommerce.admin.entity.Category;
import com.ecommerce.admin.entity.Customer;
import com.ecommerce.admin.entity.CustomerStatus;
import com.ecommerce.admin.entity.Order;
import com.ecommerce.admin.entity.OrderItem;
import com.ecommerce.admin.entity.OrderStatus;
import com.ecommerce.admin.entity.PaymentStatus;
import com.ecommerce.admin.entity.Product;
import com.ecommerce.admin.entity.User;
import com.ecommerce.admin.repository.CategoryRepository;
import com.ecommerce.admin.repository.CustomerRepository;
import com.ecommerce.admin.repository.OrderRepository;
import com.ecommerce.admin.repository.ProductRepository;
import com.ecommerce.admin.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seed() {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build());
        }

        if (categoryRepository.count() == 0) {
            Category electronics = categoryRepository.save(Category.builder().name("Electronics").description("Electronic devices").build());
            Category fashion = categoryRepository.save(Category.builder().name("Fashion").description("Clothing and accessories").build());

            Product laptop = productRepository.save(Product.builder()
                    .name("Laptop")
                    .description("Gaming laptop")
                    .price(new BigDecimal("1299.99"))
                    .stock(20)
                    .reservedStock(2)
                    .brand("Dell")
                    .active(true)
                    .category(electronics)
                    .build());

            Product shoes = productRepository.save(Product.builder()
                    .name("Running Shoes")
                    .description("Comfortable running shoes")
                    .price(new BigDecimal("89.99"))
                    .stock(12)
                    .reservedStock(1)
                    .brand("Nike")
                    .active(true)
                    .category(fashion)
                    .build());

            Customer customer = customerRepository.save(Customer.builder()
                    .firstName("Alice")
                    .lastName("Johnson")
                    .email("alice@example.com")
                    .phone("555-1111")
                    .password(passwordEncoder.encode("customer123"))
                    .status(CustomerStatus.ACTIVE)
                    .role("ROLE_CUSTOMER")
                    .build());

            Order order = new Order();
            order.setOrderNumber("ORD-1001");
            order.setCustomer(customer);
            order.setSubtotal(new BigDecimal("1389.98"));
            order.setDiscount(BigDecimal.ZERO);
            order.setShipping(BigDecimal.ZERO);
            order.setTax(BigDecimal.ZERO);
            order.setTotalAmount(new BigDecimal("1389.98"));
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setOrderStatus(OrderStatus.DELIVERED);
            OrderItem item1 = OrderItem.builder().product(laptop).quantity(1).unitPrice(laptop.getPrice()).totalPrice(laptop.getPrice()).build();
            item1.setOrder(order);
            OrderItem item2 = OrderItem.builder().product(shoes).quantity(1).unitPrice(shoes.getPrice()).totalPrice(shoes.getPrice()).build();
            item2.setOrder(order);
            order.getItems().add(item1);
            order.getItems().add(item2);
            orderRepository.save(order);
        }
    }
}
