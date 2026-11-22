package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.product.ProductCreateRequest;
import com.ecommerce.admin.dto.product.ProductResponse;
import com.ecommerce.admin.dto.product.ProductUpdateRequest;

public interface ProductService {
    PageResponse<ProductResponse> getProducts(int page, int size, String sortBy, String direction, String keyword, Long categoryId, String brand);

    ProductResponse createProduct(ProductCreateRequest request);

    ProductResponse getProduct(Long id);

    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    void deleteProduct(Long id);

    ProductResponse disableProduct(Long id);

    ProductResponse enableProduct(Long id);
}
