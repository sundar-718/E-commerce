package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.category.CategoryCreateRequest;
import com.ecommerce.admin.dto.category.CategoryResponse;
import com.ecommerce.admin.dto.category.CategoryUpdateRequest;

public interface CategoryService {
    PageResponse<CategoryResponse> getCategories(int page, int size, String sortBy, String direction);

    CategoryResponse createCategory(CategoryCreateRequest request);

    CategoryResponse getCategory(Long id);

    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request);

    void deleteCategory(Long id);
}
