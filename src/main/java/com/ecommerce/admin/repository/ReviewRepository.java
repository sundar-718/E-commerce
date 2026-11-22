package com.ecommerce.admin.repository;

import com.ecommerce.admin.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);

    List<Review> findByProductId(Long productId);
}
