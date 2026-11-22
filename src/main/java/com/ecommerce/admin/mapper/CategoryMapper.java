package com.ecommerce.admin.mapper;

import com.ecommerce.admin.dto.category.CategoryCreateRequest;
import com.ecommerce.admin.dto.category.CategoryResponse;
import com.ecommerce.admin.entity.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CategoryCreateRequest request) {
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public static void updateEntity(Category category, CategoryCreateRequest request) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
    }
}
