package com.ecommerce.admin.repository;

import com.ecommerce.admin.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByBrandContainingIgnoreCase(String brand, Pageable pageable);

    List<Product> findByStockLessThanEqual(int threshold);
}
