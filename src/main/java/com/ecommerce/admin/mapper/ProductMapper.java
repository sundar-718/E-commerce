package com.ecommerce.admin.mapper;

import com.ecommerce.admin.dto.product.ProductCreateRequest;
import com.ecommerce.admin.dto.product.ProductResponse;
import com.ecommerce.admin.entity.Category;
import com.ecommerce.admin.entity.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductCreateRequest request, Category category) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .reservedStock(0)
                .imageUrl(request.getImageUrl())
                .brand(request.getBrand())
                .active(request.getActive())
                .category(category)
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .reservedStock(product.getReservedStock())
                .imageUrl(product.getImageUrl())
                .brand(product.getBrand())
                .active(product.getActive())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static void updateEntity(Product product, ProductCreateRequest request, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setActive(request.getActive());
        product.setCategory(category);
    }
}
