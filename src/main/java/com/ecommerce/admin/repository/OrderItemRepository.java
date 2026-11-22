package com.ecommerce.admin.repository;

import com.ecommerce.admin.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProductIdAndOrderCustomerId(Long productId, Long customerId);
}
