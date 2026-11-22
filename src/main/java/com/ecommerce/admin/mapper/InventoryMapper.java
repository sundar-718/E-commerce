package com.ecommerce.admin.mapper;

import com.ecommerce.admin.dto.inventory.InventoryResponse;
import com.ecommerce.admin.entity.Product;

public final class InventoryMapper {

    private InventoryMapper() {
    }

    public static InventoryResponse toResponse(Product product) {
        return InventoryResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .availableStock(product.getStock() - product.getReservedStock())
                .reservedStock(product.getReservedStock())
                .remainingStock(product.getStock())
                .build();
    }
}
