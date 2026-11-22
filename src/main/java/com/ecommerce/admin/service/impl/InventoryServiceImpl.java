package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.inventory.InventoryResponse;
import com.ecommerce.admin.dto.inventory.InventoryUpdateRequest;
import com.ecommerce.admin.entity.Product;
import com.ecommerce.admin.exception.ResourceNotFoundException;
import com.ecommerce.admin.mapper.InventoryMapper;
import com.ecommerce.admin.repository.ProductRepository;
import com.ecommerce.admin.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;

    @Override
    public List<InventoryResponse> getInventory() {
        return productRepository.findAll().stream()
                .map(InventoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<InventoryResponse> getLowStockInventory() {
        return productRepository.findByStockLessThanEqual(5).stream()
                .map(InventoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public InventoryResponse updateReservedStock(Long productId, InventoryUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));
        product.setReservedStock(request.getReservedStock());
        return InventoryMapper.toResponse(productRepository.save(product));
    }
}
