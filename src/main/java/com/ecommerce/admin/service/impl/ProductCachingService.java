package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.product.ProductResponse;
import com.ecommerce.admin.service.ProductService;
import com.ecommerce.admin.util.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCachingService {

    private final ProductService productService;


    @Cacheable(cacheNames = CacheNames.PRODUCTS, key = "#page + ':' + #size + ':' + #sortBy + ':' + #direction + ':' + #keyword + ':' + #categoryId + ':' + #brand")
    public PageResponse<ProductResponse> getProductsPage(int page, int size, String sortBy, String direction, String keyword, Long categoryId, String brand) {
        return productService.getProducts(page, size, sortBy, direction, keyword, categoryId, brand);
    }

    @Cacheable(cacheNames = CacheNames.PRODUCT_DETAILS, key = "#id")
    public ProductResponse getProductDetails(Long id) {
        return productService.getProduct(id);
    }

    @CacheEvict(cacheNames = {CacheNames.PRODUCTS, CacheNames.PRODUCT_DETAILS, CacheNames.POPULAR_PRODUCTS}, allEntries = true)
    public void evictProductCaches() {
    }
}
