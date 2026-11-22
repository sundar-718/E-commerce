package com.ecommerce.admin.repository;

import com.ecommerce.admin.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByCustomerId(Long customerId);

    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);

    Optional<WishlistItem> findByCustomerIdAndProductId(Long customerId, Long productId);
}
