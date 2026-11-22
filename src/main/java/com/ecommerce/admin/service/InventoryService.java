package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.inventory.InventoryResponse;
import com.ecommerce.admin.dto.inventory.InventoryUpdateRequest;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getInventory();

    List<InventoryResponse> getLowStockInventory();

    InventoryResponse updateReservedStock(Long productId, InventoryUpdateRequest request);
}
