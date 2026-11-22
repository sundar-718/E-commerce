package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.product.ProductResponse;
import com.ecommerce.admin.service.ProductService;
import com.ecommerce.admin.service.impl.ProductCachingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductPublicController {

    private final ProductService productService;
    private final ProductCachingService productCachingService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice) {
        return ResponseEntity.ok(productCachingService.getProductsPage(page, size, sortBy, direction, keyword, categoryId, brand));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productCachingService.getProductDetails(id));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(productCachingService.getProductsPage(page, size, sortBy, direction, keyword, null, null));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size,
                                                                              @RequestParam(defaultValue = "name") String sortBy,
                                                                              @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(productCachingService.getProductsPage(page, size, sortBy, direction, null, categoryId, null));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByBrand(@PathVariable String brand,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam(defaultValue = "name") String sortBy,
                                                                            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(productCachingService.getProductsPage(page, size, sortBy, direction, null, null, brand));
    }
}
